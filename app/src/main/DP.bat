@echo off

:: Call java jar file to make dimens.xml files.
:: After screenMatchDP.jar,the first param is base dp.
:: And stitching the parameters that you need to fit later.
:: For example:java -jar screenMatchDP.jar 360 411 480
:: Default dps is 384 392 400 410 411 480 533 592 600 640 662 720 768 800 811 820 960 961 1024 1280 1365
@java -Dfile.encoding=utf-8 -jar %~dp0\DP.jar 360

pause
