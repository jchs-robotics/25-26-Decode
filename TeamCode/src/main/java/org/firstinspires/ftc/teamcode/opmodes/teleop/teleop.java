package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.commands.IndexCommand;
import org.firstinspires.ftc.teamcode.components.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.components.commands.ShooterCommand;
import org.firstinspires.ftc.teamcode.components.commands.TurretManualCommand;
import org.firstinspires.ftc.teamcode.components.commands.TurretSetAngleCommand;

import org.firstinspires.ftc.teamcode.components.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.components.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.components.subsystems.IndexSubsystem;
import org.firstinspires.ftc.teamcode.components.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.components.subsystems.TurretSubsystem;

@TeleOp(name = "Tele Op")
public class teleop extends CommandOpMode {

    private GamepadEx driveController;
    private GamepadEx manipulatorController;

    private DriveSubsystem driveSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private IndexSubsystem indexSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private TurretSubsystem turretSubsystem;

    private IndexCommand indexForwardCommand;
    private IndexCommand indexReverseCommand;

    private ShooterCommand shooterRPMCommand;
    private ShooterCommand shooterPowerCommand;

    private IntakeCommand intakeForwardCommand;
    private IntakeCommand intakeReverseCommand;

    private boolean indexRunningForward = false;
    private boolean indexRunningReverse = false;
    private boolean shooterRunning = false;
    private boolean intakeRunning = false;
    private boolean intakeForward = true; // true = forward, false = reverse
    private boolean useRPMControl = true;

    private double targetRPM = 6000;   // Default shooting speed
    private double shooterPower = 0.75; // Backup open-loop power
    private double intakePower = 1.0;  // Default intake power

    @Override
    public void initialize() {

        CommandScheduler.getInstance().reset();

        driveController = new GamepadEx(gamepad1);
        manipulatorController = new GamepadEx(gamepad2);

        // Initialize subsystems
        driveSubsystem = new DriveSubsystem(hardwareMap, "leftFront", "leftBack", "rightFront", "rightBack", "imu");
        driveSubsystem.initializeDrive();

        intakeSubsystem = new IntakeSubsystem(hardwareMap, "leftWrist", "rightWrist", "intakeMotor");
        indexSubsystem = new IndexSubsystem(hardwareMap, "indexMotor");
        shooterSubsystem = new ShooterSubsystem(hardwareMap, "shooterMotor");
        turretSubsystem = new TurretSubsystem(hardwareMap, "turretMotor");

        // Default turret manual control on gamepad1
        turretSubsystem.setDefaultCommand(new TurretManualCommand(
                turretSubsystem,
                () -> gamepad1.dpad_left,
                () -> gamepad1.dpad_right
        ));

        // Index commands
        indexForwardCommand = new IndexCommand(indexSubsystem, 1.0);
        indexReverseCommand = new IndexCommand(indexSubsystem, -1.0);

        // Shooter commands
        shooterRPMCommand = new ShooterCommand(shooterSubsystem, targetRPM);
        shooterPowerCommand = new ShooterCommand(shooterSubsystem, shooterPower, false);

        // Intake commands (forward/reverse)
        intakeForwardCommand = new IntakeCommand(intakeSubsystem, intakePower);
        intakeReverseCommand = new IntakeCommand(intakeSubsystem, -intakePower);
    }

    @Override
    public void run() {
        CommandScheduler.getInstance().run();

        // --- Drive Controls ---
        if (gamepad1.back) driveSubsystem.resetYaw();
        driveSubsystem.setDefaultCommand(
                driveController.getLeftX(),
                driveController.getLeftY(),
                driveController.getRightX()
        );

        // --- Index Controls ---
        if (gamepad2.y && !indexRunningForward) {
            CommandScheduler.getInstance().schedule(indexForwardCommand);
            indexRunningForward = true;
            indexRunningReverse = false;
        } else if (gamepad2.a && !indexRunningReverse) {
            CommandScheduler.getInstance().schedule(indexReverseCommand);
            indexRunningReverse = true;
            indexRunningForward = false;
        } else if (!gamepad2.y && !gamepad2.a) {
            if (indexForwardCommand.isScheduled()) indexForwardCommand.cancel();
            if (indexReverseCommand.isScheduled()) indexReverseCommand.cancel();
            indexSubsystem.stopIndex();
            indexRunningForward = false;
            indexRunningReverse = false;
        }

        // --- Shooter Controls ---
        double rightTrigger = gamepad2.right_trigger;
        double leftTrigger = gamepad2.left_trigger;

        if (rightTrigger > 0.15 && !shooterRunning) {
            if (useRPMControl) {
                CommandScheduler.getInstance().schedule(shooterRPMCommand);
            } else {
                CommandScheduler.getInstance().schedule(shooterPowerCommand);
            }
            shooterRunning = true;
        } else if (leftTrigger > 0.15 && shooterRunning) {
            if (shooterRPMCommand.isScheduled()) shooterRPMCommand.cancel();
            if (shooterPowerCommand.isScheduled()) shooterPowerCommand.cancel();
            shooterSubsystem.stopShooter();
            shooterRunning = false;
        }

        if (gamepad2.x) {
            useRPMControl = !useRPMControl;
            sleep(300); // debounce
        }

        if (gamepad2.dpad_up) {
            targetRPM += 250;
            shooterRPMCommand = new ShooterCommand(shooterSubsystem, targetRPM);
            sleep(200);
        } else if (gamepad2.dpad_down) {
            targetRPM = Math.max(500, targetRPM - 250);
            shooterRPMCommand = new ShooterCommand(shooterSubsystem, targetRPM);
            sleep(200);
        }

        // --- Intake Controls (from IntakeTeleop) ---
        boolean rightBumper = gamepad2.right_bumper;
        boolean leftBumper = gamepad2.left_bumper;

        if (rightBumper && !intakeRunning) {
            CommandScheduler.getInstance().schedule(intakeForwardCommand);
            intakeRunning = true;
            intakeForward = true;
        } else if (leftBumper && !intakeRunning) {
            CommandScheduler.getInstance().schedule(intakeReverseCommand);
            intakeRunning = true;
            intakeForward = false;
        } else if (!rightBumper && !leftBumper && intakeRunning) {
            if (intakeForwardCommand.isScheduled()) intakeForwardCommand.cancel();
            if (intakeReverseCommand.isScheduled()) intakeReverseCommand.cancel();
            intakeSubsystem.setIntake(0);
            intakeRunning = false;
        }

        // --- Turret Presets on gamepad1 ---
        if (gamepad1.y) {
            schedule(new TurretSetAngleCommand(turretSubsystem, 0.0));   // Front
        } else if (gamepad1.x) {
            schedule(new TurretSetAngleCommand(turretSubsystem, -90.0)); // Left
        } else if (gamepad1.b) {
            schedule(new TurretSetAngleCommand(turretSubsystem, 90.0));  // Right
        } else if (gamepad1.a) {
            schedule(new TurretSetAngleCommand(turretSubsystem, 180.0)); // Rear
        }

        if (gamepad1.start) {
            turretSubsystem.zeroEncoder();
        }

        // --- Telemetry ---
        telemetry.addData("Turret Angle", "%.1f", turretSubsystem.getCurrentAngle());
        telemetry.addData("Target", "%.1f", turretSubsystem.getTargetAngle());
        telemetry.addData("Shooter Running", shooterRunning);
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Current RPM", shooterSubsystem.getCurrentRPM());
        telemetry.addData("Index Forward", indexRunningForward);
        telemetry.addData("Index Reverse", indexRunningReverse);
        telemetry.addData("Intake Running", intakeRunning);
        telemetry.addData("Intake Direction", intakeForward ? "Forward" : "Reverse");
        telemetry.update();
    }
}
