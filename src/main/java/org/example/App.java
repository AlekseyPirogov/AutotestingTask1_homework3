package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Задача №1: Автоматизируйте сценарии создания проекта и создания контактного лица в CRM
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        // Задержки для функций
        int delay = 200, closeDelay = 600;
        // Набор тест-кейсов для автоматизированный тестирования:
        //addUserInCRM(delay, closeDelay);       // <-- тест-кейс №1 добавление пользователя
        addAccountInCRM(delay, closeDelay);    // <-- тест-кейс №2 добавление счета пользователю
        //delUserInCRM(delay, closeDelay);       // <-- тест-кейс №3 удаление пользователя
    }

    // Метод для инициализации драйвера браузера Google Chrome
    public static WebDriver initWebDriver(){
        // Набор настроек для браузера:
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        //options.addArguments("--headless");
        options.addArguments("start-maximized");
        // Cоздание объекта типа WebDriver с заданными опциями
        WebDriver driver = new ChromeDriver(options);
        // Переход по ссылке
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
        // Установка стандартной задержки (неявное ожидание) для браузера
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return driver;
    }

    // Метод для проверки страницы на наличие элемента
    public static void itemIsAvailable(WebDriver driver, String xpathElement) {
        // Вывод в консоль информации о работе функции
        try {
            // Установка задержки (явного ожидания) перед каждым обращением к тестируемому элементу
            WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
            webDriverWait.until(ExpectedConditions.elementToBeSelected(By.xpath(xpathElement)));
            driver.findElement(By.xpath(xpathElement));
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSupportUrl());
        }
        // Проверка условия, что xpath(xpathElement)).size() не пуст
        if(driver.findElements(By.xpath(xpathElement)).size()>0) {
            System.out.println("\nЭлемент c xpath: " + xpathElement + " найден - " + (driver.findElements(By.xpath(xpathElement)).size() > 0));
        }
    }

    // Тест-кейс №1. Добавление нового пользователя в CRM
    public static void addUserInCRM(int delay, int closeDelay) throws InterruptedException {
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver();
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Переход в меню "Bank Manager Login"
        xpathElement = ".//button[contains(.,'Bank Manager Login')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // На странице "Bank Manager Login" переход в меню "Customers" для просмотра информации о пользователях
        xpathElement = "//button[contains(.,'Customers')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // На странице "Bank Manager Login" переход в меню "Add Customer" для добавления нового пользователя
        xpathElement = "//button[contains(.,'Add Customer')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Заполнение текстовых полей информацией о новом пользователе
        xpathElement = "//input[@placeholder='First Name']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("Ivan");

        xpathElement = "//input[@placeholder='Last Name']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("Ivanov");

        xpathElement = "//input[@placeholder='Post Code']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("E77777");

        Thread.sleep(delay);
        // Добавление пользователя
        xpathElement = "//form/button[contains(.,'Add Customer')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Обработка исключения из-за появления окна c сообщением на странице
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("На сайте возникло дочернее окно c сообщением: " + alert.getText());
            Thread.sleep(delay);                    // <-- задержка
            driver.switchTo().alert().accept();     // <-- соглашаемся с alert на странице
        } catch (NoAlertPresentException e) {
            System.out.println("Сайт не выдал сообщения с подтверждением");
            e.getMessage();
        }

        // На странице "Bank Manager Login" переход в меню "Customers" для просмотра информации о пользователях
        xpathElement = "//button[contains(.,'Customers')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Выбор поля для поиска: поиск по значению "Ivan", просмотр переченя клиентов
        itemIsAvailable(driver, "//input[@placeholder='Search Customer']");
        driver.findElement(By.xpath("//input[@placeholder='Search Customer']")).click();
        itemIsAvailable(driver, "//input[@placeholder='Search Customer']");
        driver.findElement(By.xpath("//input[@placeholder='Search Customer']")).sendKeys("Ivan");

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }

    // Тест-кейс №2. Добавление банковского счета для пользователя
    public static void addAccountInCRM(int delay, int closeDelay) throws InterruptedException{
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver();
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Переход в меню "Bank Manager Login"
        xpathElement = ".//button[contains(.,'Bank Manager Login')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Переход в меню "Customers" для просмотра информации о пользователях
        xpathElement = "//button[contains(.,'Customers')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Переход в меню "Add Customer" для добавления нового пользователя
        xpathElement = "//button[contains(.,'Add Customer')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Заполнение тектовых полей с информацией о новом пользователе
        xpathElement = "//input[@placeholder='First Name']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("Ivan");

        xpathElement = "//input[@placeholder='Last Name']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("Ivanov");

        xpathElement = "//input[@placeholder='Post Code']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("E77777");

        Thread.sleep(delay);
        xpathElement = "//form/button[contains(.,'Add Customer')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Обработка исключения после появления alert c сообщением на странице
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("На сайте возникло дочернее окно c сообщением: " + alert.getText());
            Thread.sleep(delay);                    // <-- задержка
            driver.switchTo().alert().accept();     // <-- соглашаемся с alert на странице
        } catch (NoAlertPresentException e) {
            System.out.println("Сайт не выдал сообщения с подтверждением");
            e.getMessage();
        }

        // Переход в меню "Customers" для открытия счёта ("Open Account")
        xpathElement = "//button[contains(.,'Open Account')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();


        // Выбор поля для поиска: просмотр переченя клинетов, выбор клиента "Ivan Ivanov"
        itemIsAvailable(driver, "//*[@id=\"userSelect\"]/option[contains(.,'Ivan Ivanov')]");
        driver.findElement(By.xpath("//*[@id=\"userSelect\"]/option[contains(.,'Ivan Ivanov')]")).click();
        // Просмотр типов валютных счётов
        itemIsAvailable(driver, "//*[@id=\"currency\"]/option[contains(.,'Dollar')]");
        driver.findElement(By.xpath("//*[@id=\"currency\"]/option[contains(.,'Dollar')]")).click();
        // Открытие счёта
        itemIsAvailable(driver, "//form/button[contains(.,'Process')]");
        driver.findElement(By.xpath("//form/button[contains(.,'Process')]")).click();

        // Обработка исключения после появления окна c сообщением на странице
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("На сайте возникло дочернее окно c сообщением: " + alert.getText());
            Thread.sleep(delay);
            driver.switchTo().alert().accept();     // <-- соглашаемся с alert на странице
        } catch (NoAlertPresentException e) {
            System.out.println("Сайт не выдал сообщения с подтверждением");
            e.getMessage();
        }

        // На странице "Bank Manager Login" перейти в меню "Customers" для просмотра информации о пользователях
        xpathElement = "//button[contains(.,'Customers')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // Выбор поля для поиска и ввод в него значения "Ivan", просмотр перечень клинетов
        itemIsAvailable(driver, "//input[@placeholder='Search Customer']");
        driver.findElement(By.xpath("//input[@placeholder='Search Customer']")).click();
        itemIsAvailable(driver, "//input[@placeholder='Search Customer']");
        driver.findElement(By.xpath("//input[@placeholder='Search Customer']")).sendKeys("Ivan");

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }

    // Тест-кейс №3. Удаление пользователя
    public static void delUserInCRM(int delay, int closeDelay) throws InterruptedException {
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver();
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Переход в меню "Bank Manager Login"
        xpathElement = ".//button[contains(.,'Bank Manager Login')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        // На странице "Bank Manager Login" переход в меню "Customers" для просмотра информации о пользователях
        xpathElement = "//button[contains(.,'Customers')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();
        Thread.sleep(delay);

        // Выбор пользователя "Harry Potter" и его удаление
        xpathElement = "//tr[2]/td[5]/button[contains(.,'Delete')]";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }
}
