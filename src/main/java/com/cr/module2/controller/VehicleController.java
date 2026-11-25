package com.cr.module2.controller;

import com.cr.common.Result;
import com.cr.module2.entity.Vehicle;
import com.cr.module2.entity.VehicleType;
import com.cr.module2.service.VehicleService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块2：车辆管理接口
 * - /api/vehicles、/api/vehicle-types 为前台公开接口
 * - /api/admin/vehicles 为管理员接口
 */
@RestController
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/api/vehicles")
    public Result<List<Vehicle>> list(@RequestParam(required = false) Long typeId,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "false") boolean onlyAvailable) {
        return Result.ok(vehicleService.search(typeId, keyword, onlyAvailable));
    }

    @GetMapping("/api/vehicles/{id}")
    public Result<Vehicle> detail(@PathVariable Long id) {
        return Result.ok(vehicleService.getById(id));
    }

    @GetMapping("/api/vehicle-types")
    public Result<List<VehicleType>> types() {
        return Result.ok(vehicleService.listTypes());
    }

    @PostMapping("/api/admin/vehicle-types")
    public Result<Void> addType(@RequestBody VehicleType type) {
        vehicleService.addType(type.getTypeName());
        return Result.ok();
    }

    @PostMapping("/api/admin/vehicles")
    public Result<Void> add(@RequestBody Vehicle vehicle) {
        vehicleService.add(vehicle);
        return Result.ok();
    }

    @PutMapping("/api/admin/vehicles/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        vehicleService.update(id, vehicle);
        return Result.ok();
    }

    @DeleteMapping("/api/admin/vehicles/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return Result.ok();
    }
}
