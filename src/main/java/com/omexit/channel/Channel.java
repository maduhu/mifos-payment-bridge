package com.omexit.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omexit.util.DateUtil;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Antony on 2/10/2016.
 */
@Data
@Entity(name = "tbl_channel")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("channel_name")
    @Column(name = "channel_name", length = 50, unique = true, nullable = false)
    private String channelName;
    @JsonProperty("channel_endpoint")
    @Column(name = "channel_endpoint", length = 100, nullable = false)
    private String channelEndpoint;
    @JsonProperty("channel_type")
    @Column(name = "channel_type", nullable = false)
    private ChannelClassificationType channelType;
    @JsonProperty("active")
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @JsonProperty("callback")
    @Column(name = "callback", nullable = false)
    private boolean isCallback;
    @JsonIgnore
    @Column(name = "date_created", updatable = false)
    private Date dateCreated;
    @JsonIgnore
    @Column(name = "last_modified")
    private Date lastModified;
    @Transient
    @JsonProperty("date_created")
    private String strDateCreated;
    @Transient
    @JsonProperty("last_modified")
    private String strLastModified;

    @PrePersist
    protected void onCreate() {
        Date date = new Date();
        dateCreated = date;
        lastModified = date;
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }

    public String getStrDateCreated() {
        if (getDateCreated() != null) {
            strDateCreated = DateUtil.formatDate(getDateCreated(), DateUtil.DEFAULT_DATE_FORMAT);
        }

        return strDateCreated;
    }

    public String getStrLastModified() {
        if (getLastModified() != null) {
            strLastModified = DateUtil.formatDate(getLastModified(), DateUtil.DEFAULT_DATE_FORMAT);
        }
        return strLastModified;
    }
}
