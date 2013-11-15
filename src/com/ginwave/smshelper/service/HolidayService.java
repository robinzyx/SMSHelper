package com.ginwave.smshelper.service;

import java.util.List;
import java.util.Map;

public interface HolidayService {

    public boolean addHoliday(Object[] params);
    public boolean deleteHoliday(Object[] params);
    public boolean updateHoliday(Object[] params);
    
    public Map<String,String> viewHoliday(String[] selectionArgs);
    
    public List<Map<String,String>> listHolidayMaps(String[] selectionArgs);  
    
}

