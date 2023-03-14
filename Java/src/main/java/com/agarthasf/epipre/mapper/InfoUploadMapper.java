package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.InfoUpload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
public interface InfoUploadMapper extends BaseMapper<InfoUpload> {

    List<InfoUpload> getListByIdentity(String identity);

    List<InfoUpload> getAllWithName(String identity);

    List<InfoUpload> getAnomaly(String identity);
}
