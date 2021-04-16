<?php
    defined('DS') ? null : define('DS', DIRECTORY_SEPARATOR);
    defined('SITE_ROOT') ? null : define('SITE_ROOT', DS . 'xampp' .DS. 'htdocs' .DS. 'CovidApp');

    //xampp/www/CovidApp/includes
    defined('INC_PATH') ? null : define('INC_PATH', SITE_ROOT.DS.'includes');
    defined('CORE_PATH') ? null : define('CORE_PATH', SITE_ROOT.DS.'core');

    //load config file
    require_once(INC_PATH.DS."config.php");

    //core classes
    require_once(CORE_PATH.DS."user.php");
    require_once(CORE_PATH.DS."phi.php");
    require_once(CORE_PATH.DS."cdc.php");




?>