-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 15, 2021 at 09:19 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `covidapp_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `affiliation`
--

CREATE TABLE `affiliation` (
  `uid` int(10) NOT NULL,
  `nic` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `citizens_hostory`
--

CREATE TABLE `citizens_hostory` (
  `tab_id` int(10) NOT NULL,
  `uid` int(10) NOT NULL,
  `lat` varchar(70) NOT NULL COMMENT '\r\n',
  `lon` varchar(70) NOT NULL,
  `date` varchar(10) NOT NULL,
  `time` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `citizens_hostory`
--

INSERT INTO `citizens_hostory` (`tab_id`, `uid`, `lat`, `lon`, `date`, `time`) VALUES
(1, 34, '123', '123', '2021-04-15', '12:20:55'),
(2, 34, '12', '22', '2021-04-15', '12:47:56');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `uid` int(10) NOT NULL,
  `nic` varchar(12) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(60) NOT NULL,
  `dob` varchar(60) NOT NULL,
  `no` varchar(20) NOT NULL COMMENT 'address no',
  `street` varchar(70) NOT NULL COMMENT 'address street ',
  `city` varchar(70) NOT NULL COMMENT 'address city',
  `phone_num` varchar(10) NOT NULL,
  `password` text NOT NULL,
  `status` enum('Positive','Negative','Deceased','Recovered') NOT NULL COMMENT 'helath status ',
  `user_role` enum('CDC','Citizens','PHI') NOT NULL,
  `img_path` text NOT NULL COMMENT 'profile image path\r\n',
  `lat` double NOT NULL COMMENT 'current lat',
  `lon` double NOT NULL COMMENT 'current lon\r\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uid`, `nic`, `name`, `email`, `dob`, `no`, `street`, `city`, `phone_num`, `password`, `status`, `user_role`, `img_path`, `lat`, `lon`) VALUES
(1, '45', 'adfadf', 'asd', '234234', '23', 'sdf', '3423', '', '4297f44b13955235245b2497399d7a93', 'Negative', 'Citizens', '', 0, 0),
(5, '123123', 'asdasd', 'asdasd@gmail.com', '4-21-2021', '06', 'gsdfs', 'sdfsdfs', '', '25f9e794323b453885f5181f1b624d0b', '', 'Citizens', '', 0, 0),
(7, '2131', 'adsas', 'asda@gmail.com', '4-14-2021', '09', 'asdasd', 'asdasd', '', '25f9e794323b453885f5181f1b624d0b', 'Negative', 'Citizens', '', 0, 0),
(8, '234234', 'asdasd', 'asdasd@gmail.com', '4-14-2021', 'asdasd', 'asdasd', 'fgdfgdf', '', '9df62358170ff74517880ecc827d55d5', 'Negative', 'Citizens', '', 0, 0),
(22, '3423', 'asdas', 'asda@gmail.com', '4-14-2021', 'qweqwe', 'qweqwe', 'qweqwe', '', '4ba1757bf2af37bd6a2f1ae01ef4831e', 'Negative', 'Citizens', '', 0, 0),
(27, '345345', 'fsdfs', 'sdfsdf', '4-27-2021', 'dsfsdf', 'sdfsdfsd', 'sdfsdfs', '', '61b80f94cdd6d632f7bc38fd9ed91d9c', 'Negative', 'Citizens', '', 0, 0),
(32, '1234', 'Kavindu Chamath', 'kavindu@gmail.com', '4-29-2021', '06', 'new Town', 'Embilipitya', '', 'feaba01de5c4bf679ed25107d7e32091', 'Negative', 'Citizens', '', 0, 0),
(33, '223423', 'dfsdf', 'sdfsdf@gmail.com', '4-14-2021', '234234', 'sdfsdf', 'sdfsdf', '', 'f5bb0c8de146c67b44babbf4e6584cc0', 'Negative', 'Citizens', '', 0, 0),
(34, '455', 'sdfs', 'sdfsd', '15/4/1998', '23', 'gfcjg', 'vnnv', '123', '202cb962ac59075b964b07152d234b70', 'Positive', 'Citizens', 'http://192.168.1.10/CovidApp/Images/User/34.jpg', 12, 22);

--
-- Triggers `user`
--
DELIMITER $$
CREATE TRIGGER `updateCitizensHistory` AFTER UPDATE ON `user` FOR EACH ROW INSERT INTO `citizens_hostory`(`tab_id`, `uid`, `lat`, `lon`, `date`, `time`)
 VALUES (null,NEW.uid,NEW.`lat`,NEW.`lon`,now(),CURRENT_TIME())
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `citizens_hostory`
--
ALTER TABLE `citizens_hostory`
  ADD PRIMARY KEY (`tab_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uid`),
  ADD UNIQUE KEY `nic` (`nic`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `citizens_hostory`
--
ALTER TABLE `citizens_hostory`
  MODIFY `tab_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `uid` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
