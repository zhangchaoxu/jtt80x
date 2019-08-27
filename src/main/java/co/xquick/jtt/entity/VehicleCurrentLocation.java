package co.xquick.jtt.entity;

import co.xquick.jtt.b.dao.VehicleLocationDao;
import co.xquick.jtt.b.entity.VehicleLocationEntity;
import co.xquick.jtt.utils.SpringContextUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 车辆实时定位
 */
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class VehicleCurrentLocation extends Vehicle {

    /**
     * 构造VehicleCurrentLocation对象，这里的msgBody的下标必须已经移动到该对象对应的数据部分
     *
     * @param vehicleNo
     * @param vehicleColor
     * @param msgBody
     */
    public VehicleCurrentLocation(String vehicleNo, byte vehicleColor, ByteBuf msgBody) {
        this.vehicleNo = vehicleNo;
        this.vehicleColor = vehicleColor;
        this.dataType = JT809Constants.UP_EXG_MSG_REAL_LOCATION;
        this.dataLength = 36;
        location = new VehicleLocation(msgBody);
    }

    /**
     * 定位上传
     */
    @Override
    public int handlerBiz(String orgKey) {
        List<LbsUploadLocationRequest> list = Lists.newArrayList();
        LbsUploadLocationRequest request = location.convertLocation(vehicleNo);
        list.add(request);

        // 先检查一下数据
        if (location != null && request != null && request.getLocationTimeDate() != null && location.getLat() > 0 && location.getLon() > 0) {
            VehicleLocationDao dao = SpringContextUtils.getBean(VehicleLocationDao.class);
            if (location.getVec1() > 0) {
                // 速度>0
                // 保存当前位置到数据库
                VehicleLocationEntity locationEntity = new VehicleLocationEntity();
                locationEntity.setDelFlag(0);
                locationEntity.setCreateDate(new Date());
                locationEntity.setRaw(JSON.toJSONString(location));
                locationEntity.setVehicleNo(vehicleNo);
                locationEntity.setAlarm(location.getAlarm());
                locationEntity.setAltitude(location.getAltitude());
                locationEntity.setVec1(location.getVec1());
                locationEntity.setVec2(location.getVec2());
                locationEntity.setVec3(location.getVec3());
                locationEntity.setDirection(location.getDirection());
                locationEntity.setState(location.getState());
                locationEntity.setLatitude((double) location.getLat() / 1000000);
                locationEntity.setLongitude((double) location.getLon() / 1000000);
                locationEntity.setLocationTime(request.getLocationTimeDate());
                dao.insert(locationEntity);
            } else {
                // 速度为0
                VehicleLocationEntity locationEntity = dao.selectOne(new QueryWrapper<VehicleLocationEntity>()
                        .eq("vehicle_no", vehicleNo).orderByDesc("location_time").last("limit 1"));
                if (locationEntity == null) {
                    locationEntity = new VehicleLocationEntity();
                    locationEntity.setDelFlag(0);
                    locationEntity.setCreateDate(new Date());
                    locationEntity.setRaw(JSON.toJSONString(location));
                    locationEntity.setVehicleNo(vehicleNo);
                    locationEntity.setAlarm(location.getAlarm());
                    locationEntity.setAltitude(location.getAltitude());
                    locationEntity.setVec1(location.getVec1());
                    locationEntity.setVec2(location.getVec2());
                    locationEntity.setVec3(location.getVec3());
                    locationEntity.setDirection(location.getDirection());
                    locationEntity.setState(location.getState());
                    locationEntity.setLatitude((double) location.getLat() / 1000000);
                    locationEntity.setLongitude((double) location.getLon() / 1000000);
                    locationEntity.setLocationTime(request.getLocationTimeDate());
                    dao.insert(locationEntity);
                } else if (locationEntity.getLatitude() * 1000000 == location.getLat() && locationEntity.getLongitude() * 1000000 == location.getLon()) {
                    locationEntity.setLocationTime(request.getLocationTimeDate());
                    locationEntity.setUpdateDate(new Date());
                    locationEntity.setRaw(JSON.toJSONString(location));
                    locationEntity.setVehicleNo(vehicleNo);
                    locationEntity.setAlarm(location.getAlarm());
                    locationEntity.setAltitude(location.getAltitude());
                    locationEntity.setVec1(location.getVec1());
                    locationEntity.setVec2(location.getVec2());
                    locationEntity.setVec3(location.getVec3());
                    locationEntity.setDirection(location.getDirection());
                    locationEntity.setState(location.getState());
                    dao.updateById(locationEntity);
                } else {
                    locationEntity = new VehicleLocationEntity();
                    locationEntity.setDelFlag(0);
                    locationEntity.setCreateDate(new Date());
                    locationEntity.setRaw(JSON.toJSONString(location));
                    locationEntity.setVehicleNo(vehicleNo);
                    locationEntity.setAlarm(location.getAlarm());
                    locationEntity.setAltitude(location.getAltitude());
                    locationEntity.setVec1(location.getVec1());
                    locationEntity.setVec2(location.getVec2());
                    locationEntity.setVec3(location.getVec3());
                    locationEntity.setDirection(location.getDirection());
                    locationEntity.setState(location.getState());
                    locationEntity.setLatitude((double) location.getLat() / 1000000);
                    locationEntity.setLongitude((double) location.getLon() / 1000000);
                    locationEntity.setLocationTime(request.getLocationTimeDate());
                    dao.insert(locationEntity);
                }
            }
        } else {
            // do nothing
        }

        LOGGER.info("获得实时定位，{}", JSON.toJSONString(request));
        return 1;
    }

    /**
     * 定位
     * {"alarm":0,"altitude":0,"date":"CwcH4w==","direction":0,"encrypt":0,"lat":29937183,"lon":121085031,"state":3,"time":"DhEa","vec1":54,"vec2":54,"vec3":273416}
     */
    private VehicleLocation location;

}
