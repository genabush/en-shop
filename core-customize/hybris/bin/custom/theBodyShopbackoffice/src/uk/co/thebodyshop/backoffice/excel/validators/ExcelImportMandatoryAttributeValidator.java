/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Lakshmi
 **/
public class ExcelImportMandatoryAttributeValidator implements ExcelValidator {

    private static final String VALIDATION_NOT_EMPTY_MESSAGE_KEY = "excel.import.empty.error.message";
	private final List<String> listOfMandatoryAttributes;
    private CommonI18NService commonI18NService;

    @Override
    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptorModel, Map<String, Object> map) {
        return new ExcelValidationResult(new ValidationMessage(VALIDATION_NOT_EMPTY_MESSAGE_KEY, importParameters.getCellValue()));
    }

    @Override
    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptorModel) {
        boolean result=false;
        if(Objects.isNull(importParameters.getIsoCode())){
            // non localized attributes
            result=attributeCheck(importParameters,attributeDescriptorModel);
        }else if(getCommonI18NService().getCurrentLanguage().getIsocode().equals(importParameters.getIsoCode())){
            result=attributeCheck(importParameters, attributeDescriptorModel);
        }
        return result;
    }

    private boolean attributeCheck(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptorModel) {
        boolean result=false;
        Map<Boolean, List<String>> validateAttributeMap = getListOfMandatoryAttributes()
                .stream()
                .collect(Collectors.partitioningBy(attribute -> (StringUtils.equalsIgnoreCase(attributeDescriptorModel.getQualifier(),attribute) &&
                        StringUtils.isEmpty(importParameters.getCellValue().toString()))));
        List<String> isAvailable= Objects.nonNull(validateAttributeMap)?validateAttributeMap.getOrDefault(true,null):null;
        if(CollectionUtils.isNotEmpty(isAvailable)){
            result=true;
        }
        return result;
    }

    public ExcelImportMandatoryAttributeValidator(List<String> listOfMandatoryAttributes,CommonI18NService commonI18NService) {
        this.listOfMandatoryAttributes = listOfMandatoryAttributes;
        this.commonI18NService = commonI18NService;
    }

    protected List<String> getListOfMandatoryAttributes() {
        return listOfMandatoryAttributes;
    }

    protected CommonI18NService getCommonI18NService() {
        return commonI18NService;
    }
}
