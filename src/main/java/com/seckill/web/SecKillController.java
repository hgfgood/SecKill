package com.seckill.web;

import com.seckill.dto.Exposer;
import com.seckill.dto.SecKillExecution;
import com.seckill.dto.SecKillResult;
import com.seckill.entry.SecItem;
import com.seckill.enums.SecKillExecutionStatus;
import com.seckill.exception.ClosedSecKillException;
import com.seckill.exception.RepeatSecKillException;
import com.seckill.exception.SecKillException;
import com.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hgf on 16-5-22.
 */
@Controller
@RequestMapping("/seckill") //  /模块/资源/{di}/细分
public class SecKillController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillService secKillService;

    @RequestMapping(value = "/listitems", method = RequestMethod.GET)
    public String list( Model model) {
        List<SecItem> itemList = secKillService.getSecItems();
        model.addAttribute("itemList", itemList);
        Map map = model.asMap();

        //结合spring-web.xml中的ModelAndView配置，默认的前缀为“WEB-INF/jsp,后缀”.jsp“，此处返回相当与返回”WEB-INF/jsp/list.jsp“
        return "list";
    }

    @RequestMapping(value = "/{itemId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("itemId") Long itemId, Model model) {
        if (itemId == null) {
            return "redirect:/seckill/listitems";
        }
        SecItem item = secKillService.getSecItemById(itemId);
        if (item == null) {
            return "forward:/seckill/listitems";
        }
        model.addAttribute("item", item);

        return "detail";
    }


    @RequestMapping(value = "/{itemId}/exposer", method = RequestMethod.POST, produces = {"application/json;chatset=UTF-8"})
    @ResponseBody
    public SecKillResult<Exposer> exposer(@PathVariable("itemId") Long itemId) {
        SecKillResult result = null;
        try {
            Exposer exposer = secKillService.exposeSecKillUrl(itemId);
            result = new SecKillResult(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SecKillResult<Exposer>(false, e.getMessage());
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{itemId}/{md5}/execute", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public SecKillResult<SecKillExecution> execute(@PathVariable("itemId") Long itemId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone) {
        if (phone == null) {
            return new SecKillResult<SecKillExecution>(false, "未注册");
        }
//        SecKillResult<SecKillExecution> result = null;
//        try {
//            SecKillExecution execution = secKillService.executeSecKill(itemId, phone, md5);
//            result = new SecKillResult<SecKillExecution>(true, execution);
//        }catch (RepeatSecKillException e){
//            SecKillExecution execution = new SecKillExecution(itemId,SecKillExecutionStatus.REPEAT);
//            result = new SecKillResult<SecKillExecution>(true,execution);
//        }catch (ClosedSecKillException e){
//            SecKillExecution execution = new SecKillExecution(itemId,SecKillExecutionStatus.END);
//            result = new SecKillResult<SecKillExecution>(true,execution);
//        }
//        catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            SecKillExecution execution = new SecKillExecution(itemId, SecKillExecutionStatus.INNER_ERROR);
//            result = new SecKillResult<SecKillExecution>(true, execution);
//        }

        SecKillExecution secKillExecution = secKillService.executeSeckillProcedure(itemId,phone,md5);
        SecKillResult<SecKillExecution> result = new SecKillResult<SecKillExecution>(true,secKillExecution);

        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/time/now",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public SecKillResult<Long> time(){
        long now = new Date().getTime();
        SecKillResult<Long> result = new SecKillResult<Long>(true,now);

        return result;
    }



}
