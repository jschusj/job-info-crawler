package com.tianba.web.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tianba.web.mapper.JobMapper;
import com.tianba.web.model.domain.Job;
import com.tianba.web.service.IJobService;
import com.tianba.web.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 描述:采集拉钩网招聘信息
 *
 * @author yangtao
 * @create 2018-10-06 16:26
 */
@Service
@Slf4j
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    // TODO chromedriver.exe本地路径
    public static final String CHROME_DRIVER_PATH = "C:\\TianBaSoft\\soft\\chromedriver.exe";

    public static final String LAGOU_LOGIN_URL = "https://passport.lagou.com/login/login.html";
    public static final String LAGOU_HANGZHOU_JAVA_URL_START = "https://www.lagou.com/jobs/list_java?px=default&city=杭州&district=";
    public static final String LAGOU_HANGZHOU_JAVA_URL_END = "#filterBox";
    public static final String[] HANGZHOU_DISTRICT_ARR = {"西湖区", "滨江区", "余杭区", "拱墅区", "江干区", "萧山区", "下城区", "上城区", "下沙", "富阳市", "桐庐县", "临安市"};

    /** 浏览器 */
    private WebDriver driver;

    public void init() {
        log.info("初始化...");
        // 启动ChromeDriver
        startDriver();
        // 允许弹出式窗口和重定向
//        allowPopup();
        // 登录
        login();
    }

    private void login() {
        driver.get(LAGOU_LOGIN_URL);
        driverWait(1000);
        driver.findElement(By.cssSelector("body section .qr_code_content .qr_code_img")).click();
        log.info("20秒时间微信扫描登录");
        driverWait(20000);
    }

    @Override
    public void crawlJobInfo() {
//        driver.get("https://www.lagou.com/jobs/5730574.html");
        for (String district : HANGZHOU_DISTRICT_ARR) {
            String districtUrl = LAGOU_HANGZHOU_JAVA_URL_START + district + LAGOU_HANGZHOU_JAVA_URL_END;
            log.info("districtUrl=" + districtUrl);
            driver.get(districtUrl);
            List<WebElement> detailItemElements = driver.findElements(By.cssSelector("#filterCollapse .has-more .detail-area.detail-bizArea-area.toggle-target .detail-items a"));
            for (int i = 0; i < detailItemElements.size(); i++) {
                detailItemElements = driver.findElements(By.cssSelector("#filterCollapse .has-more .detail-area.detail-bizArea-area.toggle-target .detail-items a"));
                WebElement detailItemElement = detailItemElements.get(i);
                String detailText = detailItemElement.getText().trim();
                if ("不限".equals(detailText) || StringUtils.isBlank(detailText)) {
                    continue;
                }
                detailItemElement.click();
                // 翻页采集
                boolean needClickWorkPlaceBtn = turnPageCrawl("杭州", district, detailText);
                // 点击展开工作地点(没有下一页按钮时不需要展开)
                if (needClickWorkPlaceBtn) {
                    driver.findElement(By.cssSelector("#filterBrief a")).click();
                }
            }
        }
    }

    private boolean turnPageCrawl(String city, String district, String businessDistrict) {
        log.info("district=" + district + ",businessDistrict=" + businessDistrict);
        WebElement nextPageElement;
        try {
            nextPageElement = driver.findElement(By.cssSelector("#s_position_list .item_con_pager .pager_next"));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("未发现下一页按钮,url=" + driver.getCurrentUrl());
            crawlPage(city, district, businessDistrict);
            return false;
        }
        String classStr = nextPageElement.getAttribute("class");
        while (!classStr.contains("pager_next_disabled")) {
            crawlPage(city, district, businessDistrict);
            // 点击下一页
            driver.findElement(By.cssSelector("#s_position_list .item_con_pager .pager_next")).click();
            driverWait(2000);
            nextPageElement = driver.findElement(By.cssSelector("#s_position_list .item_con_pager .pager_next"));
            classStr = nextPageElement.getAttribute("class");
        }
        return true;
    }

    /**
     * 采集一页的数据
     */
    private void crawlPage(String city, String district, String businessDistrict) {
        List<WebElement> liElements = driver.findElements(By.cssSelector("#s_position_list > ul > li"));
        for (WebElement li : liElements) {
            String url = li.findElement(By.cssSelector(".position_link")).getAttribute("href");
            if (selectOne(new EntityWrapper<Job>().eq("url", url).eq("district", district).eq("business_district", businessDistrict)) != null) {
                log.info("已存在，url=" + url);
                continue;
            }
            li.findElement(By.cssSelector(".list_item_top .position .p_top h3")).click();
            switchWindow();
            try {
                crawlJob(driver.getCurrentUrl(), city, district, businessDistrict);
            } catch (Exception e) {
                e.printStackTrace();
            }
            driver.close();
            switchWindow();
            driverWait(1000);
        }
    }

    private void switchWindow() {
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> handlesList = new ArrayList<>(windowHandles);
        driver.switchTo().window(handlesList.get(handlesList.size() - 1));
    }

    private void crawlJob(String url, String city, String district, String businessDistrict) {
        driverWait(1000);
        Job job = new Job();
        job.setCity(city);
        job.setDistrict(district);
        job.setBusinessDistrict(businessDistrict);
        job.setUrl(url);
        driver.get(url);
        // 公司名称
        String company = driver.findElement(By.cssSelector(".position-head .company")).getText();
        job.setCompany(company);
        // 职位
        String name = driver.findElement(By.cssSelector(".position-head .name")).getText();
        job.setName(name);
        // 薪资
        String salary = driver.findElement(By.cssSelector(".position-head .job_request .salary")).getText().trim();
        int minSalary = 0;
        int maxSalary = 0;
        int firstKIndex = 0;
        int lastKIndex = 0;
        try {
            firstKIndex = salary.indexOf("k") == -1 ? salary.indexOf("K") : salary.indexOf("k");
            minSalary = Integer.parseInt(salary.substring(0, firstKIndex));
            lastKIndex = salary.lastIndexOf("k") == -1 ? salary.lastIndexOf("K") : salary.lastIndexOf("k");
            maxSalary = Integer.parseInt(salary.substring(salary.indexOf("-") + 1, lastKIndex));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("salary=" + salary + ",url=" + driver.getCurrentUrl() + ",firstKIndex=" + firstKIndex + ",lastKIndex=" + lastKIndex);
            return;
        }
        job.setMinSalary(minSalary);
        job.setMaxSalary(maxSalary);
        // 简述
        String request = driver.findElement(By.cssSelector(".position-head .job_request")).getText();
        job.setRequest(request);
        // 发布时间
        String publishTimeStr = driver.findElement(By.cssSelector(".position-head .job_request .publish_time")).getText().trim();

        Date publishTime;
        if (publishTimeStr.contains(":")) {
            // 13:55  发布于拉勾网
            String now = DateUtil.getStringByDate(new Date());
            publishTime = DateUtil.getDateByString(now.substring(0, now.length() - 8) + publishTimeStr.substring(0, 5) + ":00", DateUtil.yyyy_MM_dd_HH_mm_SS);
        } else if (publishTimeStr.contains("天")) {
            // 1天前  发布于拉勾网
            publishTime = DateUtil.getDateAddDays(new Date(), -Integer.parseInt(publishTimeStr.substring(0, 1)));
        } else if (publishTimeStr.contains("-")) {
            // 2019-03-19  发布于拉勾网
            publishTime = DateUtil.getDateByString(publishTimeStr.substring(0, 10), DateUtil.yyyy_MM_dd);
        } else {
            log.info("新的发布时间格式：publishTimeStr=" + publishTimeStr);
            return;
        }
        job.setPublishTime(publishTime);
        // 标签
        List<WebElement> labelElements = driver.findElements(By.cssSelector(".position-head .labels"));
        StringBuilder sb = new StringBuilder();
        for (WebElement labelElement : labelElements) {
            String label = labelElement.getText();
            sb.append(label);
            sb.append(",");
        }
        String labelStr = sb.toString();
        try {
            if (StringUtils.isNotBlank(labelStr)) {
                labelStr = labelStr.substring(0, labelStr.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("labelStr=" + labelStr + ",url=" + driver.getCurrentUrl());
            return;
        }
        job.setLabel(labelStr);
        // 职位诱惑
        String advantage = driver.findElement(By.cssSelector("#container .job-advantage p")).getText();
        job.setAdvantage(advantage);
        // 职位描述
        String detail = driver.findElement(By.cssSelector("#container .job-detail")).getText();
        job.setDetail(detail);
        // 工作地址
        String address = null;
        WebElement workAddrElement = driver.findElement(By.cssSelector("#container .job-address .work_addr"));
        if (workAddrElement != null) {
            try {
                address = workAddrElement.getText().substring(0, workAddrElement.getText().length() - 5);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("address=" + address + ",url=" + driver.getCurrentUrl());
                return;
            }
            job.setAddress(address);
        }
        // 插入数据库
        if (selectOne(new EntityWrapper<Job>().eq("url", url).eq("district", district).eq("business_district", businessDistrict)) == null) {
            job.insert();
        }
        log.info("插入一条记录，company=" + job.getCompany() + ",url=" + job.getUrl());
    }

    private List<String> getCompanyUrlList() {
        driver.get(LAGOU_HANGZHOU_JAVA_URL_START);
        List<String> hrefList = new ArrayList<>();
        // #s_position_list > div.item_con_pager > div > span.pager_next
        WebElement nextPageElement = driver.findElement(By.cssSelector("#s_position_list .item_con_pager .pager_next"));
        String classStr = nextPageElement.getAttribute("class");
        while (!classStr.contains("pager_next_disabled")) {
            // #s_position_list > ul > li.con_list_item.first_row.default_list > div.list_item_top > div.position > div.p_top > a
            // #s_position_list > ul > li:nth-child(2) > div.list_item_top > div.position > div.p_top > a
            // #s_position_list > ul > li:nth-child(3) > div.list_item_top > div.position > div.p_top > a
            // #s_position_list > ul > li > div.list_item_top > div.position > div.p_top > a
            List<WebElement> aAlements = driver.findElements(By.cssSelector("#s_position_list > ul > li > div.list_item_top > div.position > div.p_top > a"));
            for (WebElement aAlement : aAlements) {
                String href = aAlement.getAttribute("href");
                if (!hrefList.contains(href)) {
                    hrefList.add(href);
                }
            }
            // 点击下一页
            driver.findElement(By.cssSelector("#s_position_list .item_con_pager .pager_next")).click();
            driverWait(2000);
            nextPageElement = driver.findElement(By.cssSelector("#s_position_list .item_con_pager .pager_next"));
            classStr = nextPageElement.getAttribute("class");
        }
        return hrefList;
    }


    private void driverWait(Integer time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 允许弹出式窗口和重定向
     */
    private void allowPopup() {
        driver.get("chrome://settings/content/popups");
        // TODO 休眠5秒，需要手动点击，设置页面根据id找不到element
        log.info("手动点击->允许【弹出式窗口和重定向】");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动ChromeDriver
     */
    private void startDriver() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        /**
         * 隐示等待。
         * 隐性等待是指当要查找元素，而这个元素没有马上出现时
         * 告诉WebDriver查询Dom一定时间。
         * 默认值是0,但是设置之后，这个时间将在WebDriver对象实例整个生命周期都起作用。
         */
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }
}
