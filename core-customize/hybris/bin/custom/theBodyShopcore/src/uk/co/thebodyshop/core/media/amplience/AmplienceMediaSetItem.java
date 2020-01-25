/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.media.amplience;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmplienceMediaSetItem {

    public enum ItemType
    {
        img, video
    }

    public enum ItemFormat
    {
        JPEG("jpg", "image/jpeg"), PNG("png", "image/png"), MP4("mp4", "video/mp4");

        public final String extension;
        public final String mime;

        ItemFormat(final String extension, final String mime)
        {
            this.extension = extension;
            this.mime = mime;
        }

        public String getExtension()
        {
            return extension;
        }

        public String getMime()
        {
            return mime;
        }
    }

    protected static final Logger LOG = Logger.getLogger(AmplienceMediaSetItem.class);

    private ItemType type;
    private String src;
    private int width;
    private int height;
    private ItemFormat format;
    private boolean opaque;
    private String name;
    private String title;
    private Date updated;
    private int duration;
    private String description;
    private String mainLink;
    private AmplienceMediaSetItem mainThumb;
    private String mediaTypeText;
    private int mediaType;
    private int sequence;

    public ItemType getType()
    {
        return type;
    }

    public void setType(final ItemType type)
    {
        this.type = type;
    }

    public String getSrc()
    {
        return src;
    }

    public void setSrc(final String src)
    {
        this.src = src;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(final int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(final int height)
    {
        this.height = height;
    }

    public ItemFormat getFormat()
    {
        return format;
    }

    public void setFormat(final ItemFormat format)
    {
        this.format = format;
    }

    public boolean isOpaque()
    {
        return opaque;
    }

    public void setOpaque(final boolean opaque)
    {
        this.opaque = opaque;
    }

    public String getName()
    {
        if (null == name)
        {
            return src.replaceFirst(".*/([^?/]+)(\\?.*)?", "$1");
        }
        return name;
    }

    public AmplienceMediaSetItem(ItemType type, String src, int width, int height, ItemFormat format, boolean opaque, String name) {
        this.type = type;
        this.src = src;
        this.width = width;
        this.height = height;
        this.format = format;
        this.opaque = opaque;
        this.name = name;
    }

    AmplienceMediaSetItem(AmplienceMediaSetItem amplienceMediaSetItemClone){
        type = amplienceMediaSetItemClone.type;
        src = amplienceMediaSetItemClone.src;
        width = amplienceMediaSetItemClone.width;
        height = amplienceMediaSetItemClone.height;
        format = amplienceMediaSetItemClone.format;
        opaque = amplienceMediaSetItemClone.opaque;
        name = amplienceMediaSetItemClone.name;
    }

    public AmplienceMediaSetItem() {
        //NO-OP
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(final String updated)
    {
        if (null != updated)
        {
            final String dateFormat = "yyyy-MM-dd HH:mm:ss";
            final DateFormat updatedDateFormat = new SimpleDateFormat(dateFormat);
            try
            {
                this.updated = updatedDateFormat.parse(updated);
            }
            catch (final ParseException e)
            {
                LOG.warn("Unparsable string: " + updated + " by format: " + dateFormat, e);
            }
        }
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(final int duration)
    {
        this.duration = duration;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getMainLink()
    {
        return mainLink;
    }

    public void setMainLink(final String mainLink)
    {
        this.mainLink = mainLink;
    }

    public AmplienceMediaSetItem getMainThumb()
    {
        return mainThumb;
    }

    public void setMainThumb(final AmplienceMediaSetItem mainThumb)
    {
        this.mainThumb = mainThumb;
    }

    public int getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(final int mediaType)
    {
        this.mediaType = mediaType;
    }

    public int getSequence()
    {
        return sequence;
    }

    public void setSequence(final int sequence)
    {
        this.sequence = sequence;
    }

    public String getMediaTypeText()
    {
        return mediaTypeText;
    }

    public void setMediaTypeText(final String mediaTypeText)
    {
        this.mediaTypeText = mediaTypeText;
    }

}
