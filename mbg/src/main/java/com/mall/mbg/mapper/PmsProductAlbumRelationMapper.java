package com.mall.mbg.mapper;

import com.mall.mbg.model.PmsProductAlbumRelation;
import com.mall.mbg.model.PmsProductAlbumRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsProductAlbumRelationMapper {
    long countByExample(PmsProductAlbumRelationExample example);

    int deleteByExample(PmsProductAlbumRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsProductAlbumRelation row);

    int insertSelective(PmsProductAlbumRelation row);

    List<PmsProductAlbumRelation> selectByExample(PmsProductAlbumRelationExample example);

    PmsProductAlbumRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") PmsProductAlbumRelation row, @Param("example") PmsProductAlbumRelationExample example);

    int updateByExample(@Param("row") PmsProductAlbumRelation row, @Param("example") PmsProductAlbumRelationExample example);

    int updateByPrimaryKeySelective(PmsProductAlbumRelation row);

    int updateByPrimaryKey(PmsProductAlbumRelation row);
}