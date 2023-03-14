package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.pojo.Address;
import com.agarthasf.epipre.mapper.AddressMapper;
import com.agarthasf.epipre.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
