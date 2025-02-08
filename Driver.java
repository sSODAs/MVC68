import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Driver {
    private String licenseNumber;
    private String driverType;
    private String birthDate;
    private String licenseStatus;

    public Driver(String licenseNumber, String driverType, String birthDate, String licenseStatus) {
        this.licenseNumber = licenseNumber;
        this.driverType = driverType;
        this.birthDate = birthDate;
        this.licenseStatus = licenseStatus;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getDriverType() {
        return driverType;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getLicenseStatus() {
        return licenseStatus;
    }

    // คำนวณอายุจากวันเกิด
    public int calculateAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(this.birthDate, formatter);
        LocalDate currentDate = LocalDate.now();

        int age = currentDate.getYear() - birthDate.getYear();

        // ตรวจสอบถ้าคำตอบอายุยังไม่ถึงวันเกิดในปีนี้
        if (currentDate.getDayOfYear() < birthDate.getDayOfYear()) {
            age--; // ลดอายุลง 1 ปี
        }

        return age;
    }

    @Override
    public String toString() {
        return licenseNumber + "," + driverType + "," + birthDate + "," + licenseStatus;
    }

    
}
