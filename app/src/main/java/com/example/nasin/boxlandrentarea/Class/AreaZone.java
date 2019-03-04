package com.example.nasin.boxlandrentarea.Class;

public class AreaZone {
    private String zoneId;
    private String areaPic;
    private String areaType;


    public AreaZone(String zoneId, String areaPic) {
        this.zoneId = zoneId;
        this.areaPic = areaPic;
    }

    public AreaZone() {

    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public void setAreaPic(String areaPic) {
        this.areaPic = areaPic;
    }


    public String getZoneId() {
        return zoneId;
    }

    public String getAreaPic() {
        return areaPic;
    }


    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }
}
