package views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentTm {
    private String studentId;
    private String studentName;
    private String email;
    private String contact;
    private String address;
    private String nic;
}
