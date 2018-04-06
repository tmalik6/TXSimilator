/*
 * Copyright (C) 2018 tamalik
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package unideb.sd;

import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HeatMapPoint extends Circle{
    public double Lati;
    public double Longi;
    Random rand = new Random();

    public HeatMapPoint(double Lati, double Longi) {
        this.Lati = Lati;
        this.Longi = Longi;
        this.setCenterX(Lati);
        this.setCenterY(Longi);
        this.setFill(Color.RED);
        this.setRadius(1);
    }
    
    public HeatMapPoint(){
        this.Lati = random(0,500);
        this.Longi = random(0,500);
        this.setCenterX(Lati);
        this.setCenterY(Longi);
        this.setFill(Color.RED);
        this.setRadius(5);
    }

    public double getLati() {
        return Lati;
    }

    public void setLati(double Lati) {
        this.Lati = Lati;
    }

    public double getLongi() {
        return Longi;
    }

    public void setLongi(double Longi) {
        this.Longi = Longi;
    }
    
    private int random(int low, int high) {
        if (low > high) {
            return high;
        }
        int x = rand.nextInt(Integer.MAX_VALUE);
        int ret = low + (x % (high - low + 1));
        return ret;
    }
}
