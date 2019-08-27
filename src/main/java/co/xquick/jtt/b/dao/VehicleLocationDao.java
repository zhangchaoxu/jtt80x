package co.xquick.jtt.b.dao;

import co.xquick.jtt.b.entity.VehicleLocationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆定位数据
 *
 * @author Charles zhangchaoxu@gmail.com
 */
@Mapper
public interface VehicleLocationDao extends BaseMapper<VehicleLocationEntity> {
	
}