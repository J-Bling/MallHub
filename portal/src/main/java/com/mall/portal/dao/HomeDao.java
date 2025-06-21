package com.mall.portal.dao;

import com.mall.mbg.model.CmsSubject;
import com.mall.mbg.model.SmsHomeAdvertise;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface HomeDao {
    @Select("select * from sms_home_advertise where status =1 and start_time >= #{date} and end_time>=#{date} order by sort desc limit #{offset},#{limit}")
    List<SmsHomeAdvertise> recommendHomeAdvertise(@Param("date") Date date ,
                                                  @Param("offset") int offset, @Param("limit") int limit);

    @Select("select * from cms_subject where recommend_status =1 order by create_time desc limit #{offset},#{limit}")
    List<CmsSubject> recommendSubjects(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select * from cms_subject where category_id = #{category_id} and recommend_stats =1 order by create_time desc limit #{offset},#{limit}")
    List<CmsSubject> recommendSubjectsByCategory(@Param("category_id") long category_id,@Param("offset") int offset,@Param("limit") int limit);
}
