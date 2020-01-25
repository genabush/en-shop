package uk.co.thebodyshop.backoffice.renderers;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.YTestTools;
import de.hybris.platform.omsbackoffice.renderers.InvalidNestedAttributeException;
import de.hybris.platform.omsbackoffice.renderers.NestedAttributePanelRenderer;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class TbsOrderConsignmentEntriesRenderer extends NestedAttributePanelRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(TbsOrderConsignmentEntriesRenderer.class);

    private Set<ConsignmentEntryModel> entries;

    protected void renderNestedAttribute(final Component component, final Attribute attribute, final Object rootObject, final DataType rootDataType, final WidgetInstanceManager widgetInstanceManager) {
        Object nestedObject = rootObject;
        final String nestedObjectName = "";
        Attribute nestedPropertyAttribute = null;

        try {
            final List<String> splitQualifier = this.getNestedAttributeUtils().splitQualifier(attribute.getQualifier());
            final Set<ConsignmentModel> consignmentModelsSet = new HashSet<>();
            nestedObject = this.getNestedAttributeUtils().getNestedObject(nestedObject, splitQualifier.get(0));
            consignmentModelsSet.addAll((Collection) nestedObject);
            if(CollectionUtils.isEmpty(consignmentModelsSet))
            {
                return;
            }
            nestedObject = consignmentModelsSet.iterator().next();
            this.entries = consignmentModelsSet.stream().
                    map(consignmentModel -> consignmentModel.getConsignmentEntries()).
                    flatMap(consignmentEntryModels -> consignmentEntryModels.stream()).
                    collect(Collectors.toSet());
            nestedPropertyAttribute = this.generateAttributeForNestedProperty(attribute, "consignmentEntries");

            if (nestedObject == null) {
                LOG.info("Property " + nestedObjectName + " is null, skipping render of " + attribute.getQualifier());
                return;
            }

            this.nestedObjectKey = nestedObjectName + "InCurrentObject";
            widgetInstanceManager.getModel().put(this.nestedObjectKey, nestedObject);
            final String nestedObjectClass = "Consignment";
            final DataType nestedDataType = this.getTypeFacade().load(nestedObjectClass);
            final boolean canReadNestedObject = this.getPermissionFacade().canReadInstanceProperty(rootObject, nestedObjectName);
            final boolean canChangeNestedObject = this.getPermissionFacade().canChangeInstanceProperty(rootObject, nestedObjectName);
            if (canReadNestedObject && canChangeNestedObject) {
                this.createAttributeRenderer().render(component, nestedPropertyAttribute, nestedObject, nestedDataType, widgetInstanceManager);
            } else if (!canReadNestedObject) {
                final Div attributeContainer = new Div();
                attributeContainer.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed");
                this.renderNotReadableLabel(attributeContainer, nestedPropertyAttribute, rootDataType, this.getLabelService().getAccessDeniedLabel(nestedPropertyAttribute));
                attributeContainer.setParent(component);
            } else if (!canChangeNestedObject) {
                if (nestedPropertyAttribute != null) {
                    nestedPropertyAttribute.setReadonly(Boolean.TRUE);
                }

                this.createAttributeRenderer().render(component, nestedPropertyAttribute, nestedObject, nestedDataType, widgetInstanceManager);
            }
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InvalidNestedAttributeException | TypeNotFoundException ex) {
            if (LOG.isWarnEnabled()) {
                LOG.info(ex.getMessage(), ex);
            }
        }

    }


    protected Attribute generateAttributeForNestedProperty(final Attribute attribute, final String nestedQualifier) {
        final Attribute nestedAttribute = new Attribute();
        nestedAttribute.setQualifier(nestedQualifier);
        nestedAttribute.setReadonly(attribute.isReadonly());
        nestedAttribute.setDescription(attribute.getDescription());
        nestedAttribute.setEditor(attribute.getEditor());
        nestedAttribute.setLabel(attribute.getLabel());
        nestedAttribute.setVisible(attribute.isVisible());
        nestedAttribute.setMergeMode(attribute.getMergeMode());
        nestedAttribute.setPosition(attribute.getPosition());
        nestedAttribute.getEditorParameter().addAll(attribute.getEditorParameter());
        return nestedAttribute;
    }

    protected Editor createEditor(final DataType genericType, final WidgetInstanceManager widgetInstanceManager, final Attribute attribute, final Object object) {
        final DataAttribute genericAttribute = genericType.getAttribute(attribute.getQualifier());
        if (genericAttribute == null) {
            return null;
        } else {
            String editorSClass = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-editor";
            final String qualifier = genericAttribute.getQualifier();
            final String referencedModelProperty = this.nestedObjectKey + "." + attribute.getQualifier();
            final Editor editor = this.createEditor(genericAttribute, widgetInstanceManager.getModel(), referencedModelProperty);
            this.processParameters(attribute.getEditorParameter(), editor);
            final boolean editable = !attribute.isReadonly() && this.canChangeProperty(genericAttribute, object);
            if (!editable) {
                editorSClass = "ye-default-editor-readonly";
            }

            editor.setReadOnly(!editable);
            editor.setLocalized(genericAttribute.isLocalized());
            editor.setWidgetInstanceManager(widgetInstanceManager);
            editor.setEditorLabel(this.resolveAttributeLabel(attribute, genericType));
            editor.setType(this.resolveEditorType(genericAttribute));
            editor.setInitialValue(this.entries);
            editor.setOptional(!genericAttribute.isMandatory());
            YTestTools.modifyYTestId(editor, "editor_" + this.nestedObjectKey + "." + qualifier);
            editor.setAttribute("parentObject", object);
            editor.setWritableLocales(this.getPermissionFacade().getWritableLocalesForInstance(object));
            editor.setReadableLocales(this.getPermissionFacade().getReadableLocalesForInstance(object));
            if (genericAttribute.isLocalized()) {
                editor.addParameter("headerLabelTooltip", attribute.getQualifier());
                editor.addParameter("localizedEditor.attributeDescription", this.getAttributeDescription(genericType, attribute));
            }

            editor.setProperty(referencedModelProperty);
            if (StringUtils.isNotBlank(attribute.getEditor())) {
                editor.setDefaultEditor(attribute.getEditor());
            }

            editor.setPartOf(genericAttribute.isPartOf());
            editor.setOrdered(genericAttribute.isOrdered());
            editor.afterCompose();
            editor.setSclass(editorSClass);
            return editor;
        }
    }


}
