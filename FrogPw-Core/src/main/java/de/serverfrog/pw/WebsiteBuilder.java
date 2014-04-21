/*
 * Copyright (C) 2014 Bastian Venz
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
/**
 * 
 * @author serverfrog
 */

public class WebsiteBuilder {

    private WebsiteType type;
    private String address;

    public WebsiteBuilder() {
    }

    public WebsiteBuilder setType(WebsiteType type) {
        this.type = type;
        return this;
    }

    public WebsiteBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public Website createWebsite() {
        return new Website(type, address);
    }

}
