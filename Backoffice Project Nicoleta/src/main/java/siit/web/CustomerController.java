package siit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import siit.db.CustomerDao;
import siit.exceptions.ValidationException;
import siit.model.Customer;
import siit.service.CustomerService;

import java.util.Locale;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    ApplicationContext appContext;

    @GetMapping
    public ModelAndView displayCustomers() {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("customer-list");
        mav.addObject("customers", customerService.getCustomers());

        return mav;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView displayEdit(@PathVariable int id){
        ModelAndView mav = new ModelAndView("customer-edit");

        mav.addObject("customer", customerService.getCustomerByIdWithOrders(id));

        return mav;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView doEdit(@ModelAttribute Customer customer){
        try {
            customerService.updateCustomer(customer);
        } catch (ValidationException e){
            ModelAndView mav = new ModelAndView("customer-edit");
            mav.addObject("error", appContext.getMessage(e.getCode(), new Object[]{}, Locale.forLanguageTag("ro")));
            return mav;
        }
        return new ModelAndView("redirect:/customers");
    }
}
