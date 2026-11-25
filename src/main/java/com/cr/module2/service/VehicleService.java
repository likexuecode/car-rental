package com.cr.module2.service;

import com.cr.common.BizException;
import com.cr.module2.entity.Vehicle;
import com.cr.module2.entity.VehicleType;
import com.cr.module2.mapper.VehicleMapper;
import com.cr.module2.mapper.VehicleTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 模块2：车辆管理 —— 类型管理与车辆增删改查
 */
@Service
public class VehicleService {

    private final VehicleMapper vehicleMapper;
    private final VehicleTypeMapper typeMapper;

    public VehicleService(VehicleMapper vehicleMapper, VehicleTypeMapper typeMapper) {
        this.vehicleMapper = vehicleMapper;
        this.typeMapper = typeMapper;
    }

    public List<Vehicle> search(Long typeId, String keyword, boolean onlyAvailable) {
        return vehicleMapper.search(typeId, keyword, onlyAvailable);
    }

    public Vehicle getById(Long id) {
        Vehicle vehicle = vehicleMapper.findById(id);
        if (vehicle == null) {
            throw new BizException("车辆不存在");
        }
        return vehicle;
    }

    public List<VehicleType> listTypes() {
        return typeMapper.findAll();
    }

    public void addType(String typeName) {
        if (!StringUtils.hasText(typeName)) {
            throw new BizException("类型名称不能为空");
        }
        if (typeMapper.findByName(typeName) != null) {
            throw new BizException("该类型已存在");
        }
        VehicleType type = new VehicleType();
        type.setTypeName(typeName);
        typeMapper.insert(type);
    }

    /**
     * 契约2：manageVehicle —— 添加
     */
    public void add(Vehicle vehicle) {
        validate(vehicle);
        if (vehicleMapper.findByNo(vehicle.getVehicleNo()) != null) {
            throw new BizException("该编号已存在");
        }
        vehicle.setStatus(StringUtils.hasText(vehicle.getStatus()) ? vehicle.getStatus() : "可租");
        vehicleMapper.insert(vehicle);
    }

    /**
     * 契约2：manageVehicle —— 修改
     */
    public void update(Long id, Vehicle vehicle) {
        getById(id);
        validate(vehicle);
        Vehicle exists = vehicleMapper.findByNo(vehicle.getVehicleNo());
        if (exists != null && !exists.getId().equals(id)) {
            throw new BizException("该编号已存在");
        }
        vehicle.setId(id);
        if (!StringUtils.hasText(vehicle.getStatus())) {
            vehicle.setStatus("可租");
        }
        vehicleMapper.update(vehicle);
    }

    /**
     * 契约2：manageVehicle —— 删除（存在未完结订单时禁止删除）
     */
    public void delete(Long id) {
        getById(id);
        if (vehicleMapper.countUnfinishedOrders(id) > 0) {
            throw new BizException("该车辆存在未归还订单，无法删除");
        }
        vehicleMapper.delete(id);
    }

    private void validate(Vehicle vehicle) {
        if (!StringUtils.hasText(vehicle.getVehicleNo()) || !StringUtils.hasText(vehicle.getVehicleName())) {
            throw new BizException("必填项不能为空");
        }
        if (vehicle.getDailyPrice() == null || vehicle.getDailyPrice() <= 0) {
            throw new BizException("日租价必须大于0");
        }
        if (vehicle.getStock() == null || vehicle.getStock() < 0) {
            throw new BizException("库存不能小于0");
        }
    }
}
