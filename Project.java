package com.mobiledev.androidstudio.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Project model class
 */
public class Project implements Parcelable {
    
    private String id;
    private String name;
    private String description;
    private String path;
    private String type;
    private Date lastModified;
    
    /**
     * Constructor
     * 
     * @param id Project ID
     * @param name Project name
     * @param description Project description
     * @param path Project file path
     * @param type Project type (android, web, etc.)
     */
    public Project(String id, String name, String description, String path, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.path = path;
        this.type = type;
        this.lastModified = new Date();
    }
    
    /**
     * Parcelable constructor
     * 
     * @param in Parcel
     */
    protected Project(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        path = in.readString();
        type = in.readString();
        lastModified = new Date(in.readLong());
    }
    
    /**
     * Parcelable creator
     */
    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }
        
        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
    
    /**
     * Update last modified date
     */
    public void updateLastModified() {
        this.lastModified = new Date();
    }
    
    /**
     * Get ID
     * 
     * @return ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set ID
     * 
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Get name
     * 
     * @return Name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set name
     * 
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get description
     * 
     * @return Description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set description
     * 
     * @param description Description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Get path
     * 
     * @return Path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Set path
     * 
     * @param path Path
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    /**
     * Get type
     * 
     * @return Type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Set type
     * 
     * @param type Type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get last modified date
     * 
     * @return Last modified date
     */
    public Date getLastModified() {
        return lastModified;
    }
    
    /**
     * Set last modified date
     * 
     * @param lastModified Last modified date
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(path);
        dest.writeString(type);
        dest.writeLong(lastModified.getTime());
    }
    
    @Override
    public String toString() {
        return name;
    }
}