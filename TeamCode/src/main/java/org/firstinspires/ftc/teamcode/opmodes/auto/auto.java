package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.components.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.components.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.components.subsystems.PivotSubsystem;

//@Config
@Autonomous(name = "Test Auto")
public class auto extends CommandOpMode {

    // initiate classes
    private IndexSubsystem indexSubsystem; // = new IndexSubsystem();
    private TurretSubsystem turretSubsystem; // = new TurretSubsystem();
    private IntakeSubsystem intakeSubsystem; // = new IntakeSubsystem();
    private DriveSubsystem driveSubsystem; // = new DriveSubsystem();
    private ShooterSubsystem shooterSubsystem; // = new ShooterSubsystem

    private final ElapsedTime timer = new ElapsedTime();


    @Override
    public void initialize() {


        //CommandScheduler.getInstance().reset();

        indexSubsystem = new IndexSubsystem(hardwareMap, "indexMotorName");
        turretSubsystem = new TurretSubsystem(hardwareMap, "turretMotorName");
        intakeSubsystem = new IntakeSubsystem(hardwareMap, "intakeName");
        driveSubsystem = new DriveSubsystem(hardwareMap, "leftFront", "leftBack", "rightFront", "rightBack", "imu");

        driveSubsystem.initializeDrive();

        waitForStart();
        timer.reset();
    }

    @Override
    public void run() {
        // drive forward for 2 seconds
        if (timer.seconds() >= 0) {
            driveSubsystem.Drive(1, 1, 1, 1);
        }



    }













}


