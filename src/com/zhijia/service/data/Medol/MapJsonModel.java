package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class MapJsonModel {

    private String map;

    private String bus;

    private String peripheralSupport;

    private String info;

    @JsonProperty(value = "map")
    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    @JsonProperty(value = "bus")
    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    @JsonProperty(value = "peripheral_support")
    public String getPeripheralSupport() {
        return peripheralSupport;
    }

    public void setPeripheralSupport(String peripheralSupport) {
        this.peripheralSupport = peripheralSupport;
    }

    @JsonProperty(value = "info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
