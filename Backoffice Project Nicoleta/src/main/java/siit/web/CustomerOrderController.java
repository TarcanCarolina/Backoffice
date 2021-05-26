package siit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import siit.model.Order;
import siit.service.CustomerService;

import java.util.Locale;

@Controller
@RequestMapping("/customers/{customerId}/orders")
public class CustomerOrderController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ModelAndView displayCustomerOrders(@PathVariable int customerId) {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("customer-orders");
        mav.addObject("customer", customerService.getCustomerByIdWithOrders(customerId));

        return mav;
    }

    @GetMapping("/{orderId}/delete")
    public String deleteOrder(@PathVariable int customerId, @PathVariable int orderId) {
        customerService.deleteOrder(orderId);
        return "redirect:/customers/" + customerId + "/orders";
    }

    @GetMapping("/add") // /customers/{customerId}/orders/add
    public ModelAndView displayAdd(@PathVariable int customerId){
        ModelAndView mav = new ModelAndView("customer-order-add");
        mav.addObject("customer", customerService.getCustomerByIdWithOrders(customerId));
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView doAdd(@PathVariable int customerId, @ModelAttribute Order order){
        customerService.addOrderForCustomer(customerId, order);
        ModelAndView mav = new ModelAndView("redirect:/customers/" + customerId + "/orders");
        return mav;
    }

}
