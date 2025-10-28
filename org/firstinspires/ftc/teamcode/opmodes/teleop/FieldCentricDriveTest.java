package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Field Centric Drive Test")
public class FieldCentricDriveTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        DcMotorSimple leftFront = hardwareMap.get(DcMotorSimple.class, "frontleft");
        DcMotorSimple leftBack = hardwareMap.get(DcMotorSimple.class, "backleft");
        DcMotorSimple rightFront = hardwareMap.get(DcMotorSimple.class, "frontright");
        DcMotorSimple rightBack = hardwareMap.get(DcMotorSimple.class, "backright");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
        ));
        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {
            double lx = gamepad1.left_stick_x;
            double ly = gamepad1.left_stick_y;
            double rx = gamepad1.right_stick_x;

            double max = Math.max(Math.abs(lx) + Math.abs(ly) + Math.abs(rx), 1);

            double power = 0.2 + (0.6 * gamepad1.right_trigger);

            double heading = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double adjustedLx = -ly * Math.sin(heading) + lx * Math.cos(heading);
            double adjustedLy = -ly * Math.cos(heading) + lx * Math.sin(heading);

            leftFront.setPower(((adjustedLy + adjustedLx + rx) / max) * power);
            leftBack.setPower(((adjustedLy - adjustedLx + rx) / max) * power);
            rightFront.setPower(((adjustedLy - adjustedLx - rx) / max) * power);
            rightBack.setPower(((adjustedLy + adjustedLx - rx) / max) * power);
        }
    }
}
