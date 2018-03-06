package unideb.sd;
/*
 * #%L
 * TXSimilator
 * %%
 * Copyright (C) 2017-2018 Debreceni Egyetem, Informatikai Kar
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapLabel;
import com.sothawo.mapjfx.Marker;
import java.time.LocalDateTime;

public class Taxi {
    
    public int  id;
    private Coordinate coordTAX;
    private Marker markerTax;
    public LocalDateTime date;
    public double lati; 
    public double longi; 
    public MapLabel LabelTax;
    
    public int getId() {
        return id;
    }

    public void setCoordTAXD(double lati, double longi) {
        this.coordTAX = new Coordinate(lati, longi);
    }

    public Coordinate getCoordTAX() {
        return coordTAX;
    }

    public Marker getMarkerTax() {
        return markerTax;
    }

    public void setCoordTAX(Coordinate coordTAX) {
        this.coordTAX = coordTAX;
    }

    public void setMarkerTax(Marker markerTax) {
        this.markerTax = markerTax;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public Taxi(int id, LocalDateTime date, double lati, double longi) {
        this.id = id;
        this.date = date;
        this.lati = lati;
        this.longi = longi;
    }   
    
    public Taxi(double lati, double longi){
        this.lati = lati;
        this.longi = longi;
        coordTAX = new Coordinate(lati, longi);
        markerTax = Marker.createProvided(Marker.Provided.BLUE).setVisible(true);        
        markerTax.setPosition(coordTAX);
    }
    
    public void update(double lati, double longi){
        this.lati = lati;
        this.longi = longi;
        coordTAX = new Coordinate(lati, longi);
        markerTax = Marker.createProvided(Marker.Provided.BLUE).setVisible(true);        
        markerTax.setPosition(coordTAX);
    }
    
    public Marker getMarker(){
        return this.markerTax;
    }

    public MapLabel getLabelTax() {
        return LabelTax;
    }

    public void setLabelTax(MapLabel LabelTax) {
        this.LabelTax = LabelTax;
    }    
}