package com.shagaba.jacksync;

public interface Syncable {

	/**
     * @return the version
     */
    public Long getVersion();

    /**
     * @param version the version to set
     */
    public void setVersion(Long version);

}