# Cradle App

Real-world application that uses the Self-shopping APIs to control the locking cradle. Currently, this app works for Datalogic `Memor 1` and `Joya Touch A6` Android devices. Color is used to indicate battery charge level.

## Notes

* App starts in the background on boot.
* You can manually start if desired before reboot if desired
* Either way, the app only displays if the device is inserted into the locking cradle.

## Screenshots

| Fullscreen Message | High battery, >65% | Medium battery | Low battery, < 30% |
|--------------------|--------------|----------------|-------------|
![Full screen message](screenshots/screen_green_viewing_fullscreen_message.png) | ![High battery](screenshots/screen_green.png) | ![Medium battery](screenshots/screen_yellow.png) | ![Low battery](screenshots/screen_red.png)
