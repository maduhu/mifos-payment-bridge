
package com.omexit.mifos.portfolio.client;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "accountNo",
    "subStatus",
    "active",
    "activationDate",
    "firstname",
    "middlename",
    "lastname",
    "displayName",
    "mobileNo",
    "dateOfBirth"
})
public class Client {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("accountNo")
    private String accountNo;
    @JsonProperty("subStatus")
    private SubStatus subStatus;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("activationDate")
    private List<Long> activationDate = new ArrayList<Long>();
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("middlename")
    private String middlename;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("mobileNo")
    private String mobileNo;
    @JsonProperty("dateOfBirth")
    private List<Long> dateOfBirth = new ArrayList<Long>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The accountNo
     */
    @JsonProperty("accountNo")
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * 
     * @param accountNo
     *     The accountNo
     */
    @JsonProperty("accountNo")
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    /**
     * 
     * @return
     *     The subStatus
     */
    @JsonProperty("subStatus")
    public SubStatus getSubStatus() {
        return subStatus;
    }

    /**
     * 
     * @param subStatus
     *     The subStatus
     */
    @JsonProperty("subStatus")
    public void setSubStatus(SubStatus subStatus) {
        this.subStatus = subStatus;
    }

    /**
     * 
     * @return
     *     The active
     */
    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 
     * @return
     *     The activationDate
     */
    @JsonProperty("activationDate")
    public List<Long> getActivationDate() {
        return activationDate;
    }

    /**
     * 
     * @param activationDate
     *     The activationDate
     */
    @JsonProperty("activationDate")
    public void setActivationDate(List<Long> activationDate) {
        this.activationDate = activationDate;
    }

    /**
     * 
     * @return
     *     The firstname
     */
    @JsonProperty("firstname")
    public String getFirstname() {
        return firstname;
    }

    /**
     * 
     * @param firstname
     *     The firstname
     */
    @JsonProperty("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * 
     * @return
     *     The middlename
     */
    @JsonProperty("middlename")
    public String getMiddlename() {
        return middlename;
    }

    /**
     * 
     * @param middlename
     *     The middlename
     */
    @JsonProperty("middlename")
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    /**
     * 
     * @return
     *     The lastname
     */
    @JsonProperty("lastname")
    public String getLastname() {
        return lastname;
    }

    /**
     * 
     * @param lastname
     *     The lastname
     */
    @JsonProperty("lastname")
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The displayName
     */
    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @return
     *     The mobileNo
     */
    @JsonProperty("mobileNo")
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * 
     * @param mobileNo
     *     The mobileNo
     */
    @JsonProperty("mobileNo")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * 
     * @return
     *     The dateOfBirth
     */
    @JsonProperty("dateOfBirth")
    public List<Long> getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * 
     * @param dateOfBirth
     *     The dateOfBirth
     */
    @JsonProperty("dateOfBirth")
    public void setDateOfBirth(List<Long> dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
