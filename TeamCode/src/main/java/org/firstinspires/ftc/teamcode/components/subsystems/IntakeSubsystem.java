package org.firstinspires.ftc.teamcode.components.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSubsystem extends SubsystemBase {

    // Using a GoBILDA DC motor instead of a CRServo for intake
    private DcMotor intakeMotor;

    private double intakePower;

    public IntakeSubsystem(final HardwareMap hMap, final String intakeName) {
        // Initialize the intake motor
        intakeMotor = hMap.get(DcMotor.class, intakeName);

        // Reverse direction if needed depending on physical orientation
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set to zero power on startup
        intakeMotor.setPower(0);
    }

    // Spin intake forward or backward
    public void setIntake(double power) {
        intakePower = power;
        intakeMotor.setPower(intakePower);
    }

    // Called periodically in your OpMode loop
    public void setDefaultCommand(boolean Lump, boolean Rump) {

        // Basic control example:
        if (Rump) {
            intakePower = 1.0;   // Intake in
        } else if (Lump) {
            intakePower = -1.0;  // Intake out
        } else {
            intakePower = 0.0;   // Stop intake
        }

        intakeMotor.setPower(intakePower);
    }

    @Override
    public void periodic() {
        // Telemetry or state tracking can go here if needed
    }
}
