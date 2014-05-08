/*
 * Copyright (C) 2014 Serverfrog.de
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
package de.serverfrog.pw;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Bastian Venz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration implements Serializable{

    private Website website;
    
    private int lenght;
    
    private boolean shouldSmall;
    
    private boolean shouldBig;
    
    private boolean shouldNumbers;
    
    private boolean shouldSpecial;
    
    private int smallWeigth;
    
    private int bigWeigth;
    
    private int numbersWeigth;
    
    private int specialWeigth;
    
    private String specialChars;

    public Configuration(String specialChars) {
        this.specialChars = specialChars;
    }
   
    

}
