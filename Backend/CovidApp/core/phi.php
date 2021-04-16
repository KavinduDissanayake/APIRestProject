
<?php

class PHI {

    private $con;
    private $table='user';

    //post properties 
    public $uid;
    public $nic;
    public $name;
    public $email;
    public $dob;
    public $no;
    public $street;
    public $city;
    public $phone_num;
    public $status;
    public $history_lat;
    public $history_lon;
    public $img_path;
    public $user_role;
    public $encodedImage;
    public $uploade_url;

  //counstructor with db connection 
  public function __construct($db) {
    $this->con = $db;
}



    //update user info funcation 

public function user_status_update_phi(){

    $query="UPDATE $this->table SET  `status`=:status
    WHERE `nic`= :nic";


                $stmt = $this -> con -> prepare($query);

                //clean data
                $this->nic = htmlspecialchars(strip_tags($this->nic));
                $this->status = htmlspecialchars(strip_tags($this->status));
 
                
              



                //binding parameters 
                $stmt->bindParam(':nic', $this->nic);
                $stmt->bindParam(':status', $this->status);

               

                      try {

                        if($stmt->execute()) { return true; }

                        }catch(PDOException $e){      
                            
                            // echo $e->getMessage();
                            return false;  
                        }      
                        
          
                        
          

}





 
public function read_citizen_history() {
        $query = "SELECT `nic`,`name`,`email`,`dob`,`no`,`street`,`city`,`phone_num`,`status`,citizens_hostory.lat as 'history_lat',citizens_hostory.lon
        as 'history_lon',citizens_hostory.date,citizens_hostory.time
        from user
        
        INNER join 
        citizens_hostory on user.uid=citizens_hostory.uid
        Where nic= ?  ;";

    //echo $query;

    //prapre statement
        $stmt = $this -> con -> prepare($query);

        //binding parm
        $stmt->bindParam(1,$this->nic);

        //execute the quary
        $stmt->execute();



        return $stmt;
}




}


?>