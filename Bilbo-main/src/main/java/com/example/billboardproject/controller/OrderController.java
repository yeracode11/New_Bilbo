package com.example.billboardproject.controller;

import com.example.billboardproject.model.Order;
import com.example.billboardproject.model.User;
import com.example.billboardproject.service.BillboardService;
import com.example.billboardproject.service.OrderService;
import com.example.billboardproject.service.impl.CityServiceImpl;
import com.example.billboardproject.service.impl.LocationServiceImpl;
import com.example.billboardproject.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BillboardService billboardService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CityServiceImpl cityService;
    @Autowired
    private LocationServiceImpl locationService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/orderBillboard")
    public String orderBillboard(@RequestParam(name = "startDate") String startDate,
                                 @RequestParam(name = "endDate") String endDate,
                                 @RequestParam(name = "phone") String phone,
                                 @RequestParam(name = "billboardId") String billboardId, Model model) {
        String redirect = "";
        User currentUser = userService.getUserData();
        Order order = new Order();
        if (currentUser != null) {
            order.setStartDate(startDate);
            order.setCreatedAt(LocalDateTime.now());
            order.setEndDate(endDate);
            order.setBillboard(billboardService.getBillboardById((long) Integer.parseInt(billboardId)));
            order.setTelNumber(phone);
            order.setUser(currentUser);
            order.setStatus(0);
        }
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("locations", locationService.getAllLocations());


        if (orderService.addOrder(order)) {
            redirect = "redirect:/detailBillboard/" + billboardId + "/?successful";
        } else {
            redirect = "redirect:/detailBillboard/" + billboardId + "/?error";
        }

        return redirect;
    }
}
