// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final RomiDrivetrain m_drivetrain = new RomiDrivetrain();
  private int Step = 1;
  private int Spiralstep = 1;

  private RomiGyro gyro = new RomiGyro();

  private int SquareCircle = 1;
  private int SpiralTime = 1;

  public void square() {
    if(Step == 1) {
      m_drivetrain.arcadeDrive(0.5, 0);
      if(m_drivetrain.getRightDistanceInch() >= 12) {
        Step = 2;
        m_drivetrain.resetEncoders();
      }
    } else {
      m_drivetrain.arcadeDrive(0, 0.5);
      if(gyro.getAngleZ() >= 90) {
        Step = 1;
        gyro.reset();
      }
    }
  }

  public void circle() {
    m_drivetrain.arcadeDrive(0.7, 0.5);
  }

  private double SpiralSpeed = 1;

  public void spiral() {
    if(SpiralTime < 50) {
      m_drivetrain.arcadeDrive(0.7, SpiralSpeed);
      SpiralTime = SpiralTime + 1;
    } else {
      SpiralTime = 1;
    if (Spiralstep == 1) {
      SpiralSpeed = SpiralSpeed - 0.1;
      if (SpiralSpeed <= 0.4) {
        Spiralstep = 2;
      }
    } else {
      SpiralSpeed = SpiralSpeed + 0.1;
      if (SpiralSpeed >= 1) {
        Spiralstep = 1;
      }
    }
    }
  }

  // This line creates a new controller object, which we can use to get inputs from said controller/joystick.
  private GenericHID controller = new GenericHID(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SpiralSpeed = 0.8;
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    m_drivetrain.resetEncoders();
    gyro.reset();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        if(SquareCircle < 500) {
          square();
          SquareCircle = SquareCircle + 1;
        } else {
          circle();
          SquareCircle = SquareCircle + 1;
          if(SquareCircle > 1000) {
            SquareCircle = 0;
          }
        }
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // WARN: The controller code is written based on my controller, and as such may need to be changed

    // The getRawAxis method allows one to get the value an axis is on
    // We use axis to get stuff from the joysticks, as it is easy to represent a joystick
    // like a coordinate grid, which allows us to just extract the x or y axis information from it.
    double forwardSpeed = -controller.getRawAxis(1);
    double turnSpeed = -controller.getRawAxis(0);

    m_drivetrain.arcadeDrive(forwardSpeed, turnSpeed);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    spiral();
  }
}