package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Timed + Telemetry (ShooterBot)")
public class timedDrive extends OpMode {
    // --- Timer ---
    private final ElapsedTime runtime = new ElapsedTime();

    // --- Drivetrain ---
    private DcMotor leftFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightFrontMotor;
    private DcMotor rightBackMotor;

    // --- Mechanisms ---
    private DcMotor intake;
    private DcMotor index;
    private DcMotor shooter;
    private DcMotor turret;

    // --- IMU ---
    private IMU imu;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");

        // --- Drive Motors ---
        leftFrontMotor  = hardwareMap.get(DcMotor.class, "leftFront");
        leftBackMotor   = hardwareMap.get(DcMotor.class, "leftBack");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFront");
        rightBackMotor  = hardwareMap.get(DcMotor.class, "rightBack");

        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotor.Direction.FORWARD);

        // --- Mechanism Motors ---
        intake  = hardwareMap.get(DcMotor.class, "intake");
        index   = hardwareMap.get(DcMotor.class, "index");
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        turret  = hardwareMap.get(DcMotor.class, "turret");

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        index.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE); // adjust if needed
        turret.setDirection(DcMotorSimple.Direction.FORWARD);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        index.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // --- IMU Setup ---
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)
        );
        imu.initialize(parameters);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // --- Drivetrain Control ---
        double y = -gamepad1.left_stick_y;  // forward/back
        double x = gamepad1.left_stick_x;   // strafe
        double rx = gamepad1.right_stick_x; // rotate robot

        // Reset heading
        if (gamepad1.back) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Field-oriented drive
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
        rotX *= 1.1; // Counteract imperfect strafing

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower  = (rotY + rotX + rx) / denominator;
        double backLeftPower   = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower  = (rotY + rotX - rx) / denominator;

        leftFrontMotor.setPower(frontLeftPower);
        leftBackMotor.setPower(backLeftPower);
        rightFrontMotor.setPower(frontRightPower);
        rightBackMotor.setPower(backRightPower);

        // --- Turret Control (D-pad left/right on gamepad1) ---
        if (gamepad1.dpad_right) {
            turret.setPower(0.5);   // rotate right
        } else if (gamepad1.dpad_left) {
            turret.setPower(-0.5);  // rotate left
        } else {
            turret.setPower(0);     // stop
        }

        // --- Mechanism Controls (gamepad2) ---

        // Intake control
        if (gamepad2.right_bumper) { // intake in
            intake.setPower(1);
        } else if (gamepad2.left_bumper) { // intake out
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }

        // Shooter control (triggers)
        double shooterPower = gamepad2.right_trigger - gamepad2.left_trigger;
        shooter.setPower(shooterPower);

        // Index control (Y = forward, A = reverse)
        if (gamepad2.y) {
            index.setPower(1);
        } else if (gamepad2.a) {
            index.setPower(-1);
        } else {
            index.setPower(0);
        }

        // --- Telemetry ---
        telemetry.addData("Run Time", runtime.toString());
        telemetry.addData("Turret Power", "%.2f", turret.getPower());
        telemetry.addData("Intake Power", "%.2f", intake.getPower());
        telemetry.addData("Index Power", "%.2f", index.getPower());
        telemetry.addData("Shooter Power", "%.2f", shooter.getPower());
        telemetry.addData("Heading (deg)", "%.1f",
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        telemetry.update();
    }
}
