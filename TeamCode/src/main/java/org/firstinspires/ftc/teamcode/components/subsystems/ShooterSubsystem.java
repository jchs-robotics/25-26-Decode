package org.firstinspires.ftc.teamcode.components.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ShooterSubsystem extends SubsystemBase {

    private final DcMotorEx shooterMotor;

    // Shooter constants
    private static final double TICKS_PER_REV = 383.6; // Example for GoBILDA 5202 series motor
    private static final double GEAR_RATIO = 1.0;      // Change if geared up/down
    private static final double RPM_TO_TICKS_PER_SEC = (TICKS_PER_REV * GEAR_RATIO) / 60.0;

    private double targetRPM = 0.0;

    public ShooterSubsystem(final HardwareMap hMap, final String shooterName) {
        shooterMotor = hMap.get(DcMotorEx.class, shooterName);

        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Use encoder-based velocity control
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Start at zero power
        shooterMotor.setPower(0);
    }

    /**
     * Sets the shooter motor to a specific RPM using built-in velocity PID.
     *
     * @param rpm target speed in revolutions per minute
     */
    public void setShooterRPM(double rpm) {
        targetRPM = rpm;
        double ticksPerSecond = rpm * RPM_TO_TICKS_PER_SEC;
        shooterMotor.setVelocity(ticksPerSecond);
    }

    /**
     * Manually set power (0 to 1) if you don't want PID control.
     */
    public void setShooterPower(double power) {
        targetRPM = 0.75; // clear target RPM
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor.setPower(power);
    }

    /**
     * Stops the shooter motor.
     */
    public void stopShooter() {
        shooterMotor.setPower(0);
        targetRPM = 0;
    }

    /**
     * Returns the current motor velocity in RPM.
     */
    public double getCurrentRPM() {
        double ticksPerSecond = shooterMotor.getVelocity();
        return ticksPerSecond / RPM_TO_TICKS_PER_SEC;
    }

    /**
     * Returns the current target RPM.
     */
    public double getTargetRPM() {
        return targetRPM;
    }

    @Override
    public void periodic() {
        // You could send telemetry here:
        // telemetry.addData("Shooter RPM", getCurrentRPM());
    }
}
