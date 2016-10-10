package com.hugo.lucky.easyparking;

import java.io.Serializable;

/**
 * Created by lucky on 8/4/2016.
 */
public class PostedParkingLot implements Serializable {
    String address,city,endTime,ownerId,ownerName,reserverId,state,startTime,name,primaryID,email;
    Long price;
    Boolean reserved;
    Double lattitude;
    Double longitude;
    public PostedParkingLot() {

    }
    public String getEmail(){return email;}
    public void setEmail(String _email){email=_email;}

    public Double getLattitude(){return lattitude;}
    public void setLattitude(Double _lat){lattitude=_lat;}

    public Double getLongitude(){return longitude;}
    public void setLongitude(Double _log){longitude=_log;}

    public String getPrimaryID() {
        return primaryID;
    }
    public void setPrimaryID(String primaryID) {
        this.primaryID = primaryID;
    }

    public String getAddress(){return address;}
    public void setAddress(String _address){this.address=_address;}

    public String getCity() {return city;}
    public void setCity(String _city){this.city=_city;}

    public String getEndTime() {return endTime;}
    public void setEndTime(String _endTime){this.endTime=_endTime;}

    public String getOwnerId(){return ownerId;}
    public void setOwnerId(String _ownerId){this.ownerId=_ownerId;}

    public String getOwnerName(){return ownerName;}
    public void setOwnerName(String _ownerName){this.ownerName=_ownerName;}

    public String getReserverId(){return reserverId;}
    public void setReserverId(String _reserverId){this.reserverId=_reserverId;}

    public String getState(){return state;}
    public void setState(String _state){this.state=_state;}

    public String getStartTime(){return startTime;}
    public void setStartTime(String _startTime){this.startTime=_startTime;}

    public String getName(){return name;}
    public void setName(String _name){this.name=_name;}

    public Long getPrice(){return price;}
    public void setPrice(Long _price){this.price=_price;}

    public Boolean getReserved(){return reserved;}
    public void setReserved(Boolean _reserved){this.reserved=_reserved;}
}
