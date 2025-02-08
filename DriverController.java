import java.util.List;
import java.util.Random;

public class DriverController {
    private List<Driver> drivers;

    public DriverController() {
        this.drivers = DriverDatabase.loadDrivers();
    }

    public String checkLicense(String licenseNumber) {
        if (!licenseNumber.matches("[1-9][0-9]{8}")) {
            return "หมายเลขไม่ถูกต้อง! ต้องเป็นตัวเลข 9 หลักและไม่ขึ้นต้นด้วย 0";
        }

        Driver driver = drivers.stream()
                .filter(d -> d.getLicenseNumber().equals(licenseNumber))
                .findFirst()
                .orElse(null);

        if (driver == null) {
            return "หมายเลขใบขับขี่ไม่พบในฐานข้อมูล";
        }

        String driverInfo = "หมายเลขใบขับขี่: " + driver.getLicenseNumber() + "\n" +
                            "ประเภท: " + driver.getDriverType() + "\n" +
                            "สถานะ: " + driver.getLicenseStatus();

        return driverInfo + "\n\n" + checkDriverStatus(driver);
    }

    private String checkDriverStatus(Driver driver) {
        int age = driver.calculateAge(); // คำนวณอายุจากวันเกิด
        String status = driver.getLicenseStatus();
        
        // แสดงอายุของผู้ขับขี่
        String ageInfo = "อายุ: " + age + " ปี";
    
        // ตรวจสอบประเภทของผู้ขับขี่
        switch (driver.getDriverType()) {
            case "มือใหม่":
                if (age < 16) {
                    return "ตรวจสอบ: มือใหม่ (" + ageInfo + "): ผู้ขับขี่อายุต่ำกว่า 16 ปี สถานะ: ถูกระงับ";
                } else if (age > 50) {
                    return "ตรวจสอบ: มือใหม่ (" + ageInfo + "): ผู้ขับขี่อายุเกิน 50 ปี สถานะ: หมดอายุ";
                }
                return "ตรวจสอบ: มือใหม่ (" + ageInfo + "): สถานะ: " + status;
            case "คนขับรถสาธารณะ":
                if (age < 20) {
                    return "ตรวจสอบ: คนขับรถสาธารณะ (" + ageInfo + "): ผู้ขับขี่อายุต่ำกว่า 20 ปี สถานะ: ถูกระงับ";
                } else if (age > 60) {
                    return "ตรวจสอบ: คนขับรถสาธารณะ (" + ageInfo + "): ผู้ขับขี่อายุเกิน 60 ปี สถานะ: หมดอายุ";
                }
                Random rand = new Random();
                int complaints = rand.nextInt(11);  // จำนวนการร้องเรียน 0 - 10
    
                if (complaints > 5) {
                    return "ตรวจสอบ: คนขับรถสาธารณะ (" + ageInfo + "): จำนวนการร้องเรียน " + complaints + " ครั้ง - สถานะ: ถูกระงับชั่วคราว";
                } else {
                    return "ตรวจสอบ: คนขับรถสาธารณะ (" + ageInfo + "): จำนวนการร้องเรียน " + complaints + " ครั้ง - สถานะ: " + status;
                }
            case "บุคคลทั่วไป":
                if (age < 16) {
                    return "ตรวจสอบ: บุคคลทั่วไป (" + ageInfo + "): ผู้ขับขี่อายุต่ำกว่า 16 ปี สถานะ: ถูกระงับ";
                } else if (age > 70) {
                    return "ตรวจสอบ: บุคคลทั่วไป (" + ageInfo + "): ผู้ขับขี่อายุเกิน 70 ปี สถานะ: หมดอายุ";
                }
                return "ตรวจสอบ: บุคคลทั่วไป (" + ageInfo + "): สถานะ: " + status;
            default:
                return "ไม่รู้จักประเภทผู้ขับขี่";
        }
    }

    public void addDriver(String licenseNumber, String driverType, String birthDate, String licenseStatus) {
        Driver newDriver = new Driver(licenseNumber, driverType, birthDate, licenseStatus);
        drivers.add(newDriver);
        DriverDatabase.saveDrivers(drivers);
    }

    public String getDriverReport() {
    int newDrivers = 0;
    int publicDrivers = 0;
    int generalDrivers = 0;
    
    // สถานะของผู้ขับขี่
    int suspended = 0;
    int expired = 0;
    int active = 0;

    // นับจำนวนผู้ขับขี่ตามประเภทและสถานะ
    for (Driver driver : drivers) {
        switch (driver.getDriverType()) {
            case "มือใหม่":
                newDrivers++;
                break;
            case "คนขับรถสาธารณะ":
                publicDrivers++;
                break;
            case "บุคคลทั่วไป":
                generalDrivers++;
                break;
        }

        switch (driver.getLicenseStatus()) {
            case "ถูกระงับ":
                suspended++;
                break;
            case "หมดอายุ":
                expired++;
                break;
            case "ปกติ":
                active++;
                break;
        }
    }

    // สร้างรายงาน
    return "<html>มือใหม่: " + newDrivers + " คน<br>" +
           "คนขับรถสาธารณะ: " + publicDrivers + " คน<br>" +
           "บุคคลทั่วไป: " + generalDrivers + " คน<br><br>" +
           "ถูกระงับ: " + suspended + " คน<br>" +
           "หมดอายุ: " + expired + " คน<br>" +
           "ปกติ: " + active + " คน</html>";
}

}
