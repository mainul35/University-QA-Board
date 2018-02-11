/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 100116
 Source Host           : localhost:3306
 Source Schema         : coaching

 Target Server Type    : MySQL
 Target Server Version : 100116
 File Encoding         : 65001

 Date: 01/02/2018 13:17:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for screencast
-- ----------------------------
DROP TABLE IF EXISTS `screencast`;
CREATE TABLE `screencast`  (
  `id` bigint(20) NOT NULL,
  `date` date DEFAULT NULL,
  `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `url` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `tag_subject_id` bigint(20) DEFAULT NULL,
  `userEntity_user_uuid` bigint(20) DEFAULT NULL,
  `album_album_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKj7q1gdqn5e8bhchre1jbk46ug`(`album_album_id`) USING BTREE,
  INDEX `FKcs2yc0jgo3q635p1f426g0ufb`(`tag_subject_id`) USING BTREE,
  INDEX `FKtq1k85ws283q5ce7kwd6ecmyt`(`userEntity_user_uuid`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of screencast
-- ----------------------------
INSERT INTO `screencast` VALUES ('1517296655390', '2018-01-30', 'Handnote', 'Lesson 1', 'C://temp//1517235954545//1517296655390.pdf', '1517258390951', NULL, '1517296212163');
INSERT INTO `screencast` VALUES ('1517443700944', '2018-02-01', 'This is a word document of lesson 1.', 'Lesson 1', 'C://temp//1517284898983//1517443700944.pdf', '1517258390951', NULL, '1517443160074');

-- ----------------------------
-- Table structure for tbl_album
-- ----------------------------
DROP TABLE IF EXISTS `tbl_album`;
CREATE TABLE `tbl_album`  (
  `album_id` bigint(20) NOT NULL,
  `album_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `date` date DEFAULT NULL,
  `album_description` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `tag_subject_id` bigint(20) DEFAULT NULL,
  `userEntity_user_uuid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`album_id`) USING BTREE,
  INDEX `FKowm97re6iefct9wibp5y3ol4`(`tag_subject_id`) USING BTREE,
  INDEX `FKc09dhwj8fi15m9dnvl05xh9ai`(`userEntity_user_uuid`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_album
-- ----------------------------
INSERT INTO `tbl_album` VALUES ('1517296212163', 'Class vii Bangla lectures', '2018-01-30', 'Bangla lectures for class vii', '1517258390951', '1517235954545');
INSERT INTO `tbl_album` VALUES ('1517443160074', 'Album 1', '2018-02-01', 'This is a word document of lesson 1.', '1517258390951', '1517284898983');

-- ----------------------------
-- Table structure for tbl_album_screencast
-- ----------------------------
DROP TABLE IF EXISTS `tbl_album_screencast`;
CREATE TABLE `tbl_album_screencast`  (
  `Album_album_id` bigint(20) NOT NULL,
  `screenCasts_id` bigint(20) NOT NULL,
  UNIQUE INDEX `UK_66mg1py1kp3qwx0mnbk4xk7go`(`screenCasts_id`) USING BTREE,
  INDEX `FKlol2vyjno7lioapavfx7dc0ec`(`Album_album_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for tbl_authority
-- ----------------------------
DROP TABLE IF EXISTS `tbl_authority`;
CREATE TABLE `tbl_authority`  (
  `role_uuid` bigint(20) NOT NULL,
  `authority` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`role_uuid`) USING BTREE,
  UNIQUE INDEX `UK_3quenquhon50202nm8nlitrls`(`authority`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_authority
-- ----------------------------
INSERT INTO `tbl_authority` VALUES ('1517135749622', 'ROLE_POTENTIAL_TEACHER');
INSERT INTO `tbl_authority` VALUES ('1517136134152', 'ROLE_ADMIN');
INSERT INTO `tbl_authority` VALUES ('1517162726118', 'ROLE_TEACHER');
INSERT INTO `tbl_authority` VALUES ('1517307845407', 'ROLE_STUDENT');

-- ----------------------------
-- Table structure for tbl_group_role
-- ----------------------------
DROP TABLE IF EXISTS `tbl_group_role`;
CREATE TABLE `tbl_group_role`  (
  `groupRoleId` bigint(20) NOT NULL,
  `groupRoleName` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`groupRoleId`) USING BTREE,
  UNIQUE INDEX `UK_5ihk053dyv2xhfyyxcvdxwi77`(`groupRoleName`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_subject
-- ----------------------------
DROP TABLE IF EXISTS `tbl_subject`;
CREATE TABLE `tbl_subject`  (
  `subject_id` bigint(20) NOT NULL,
  `subject_name` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`subject_id`) USING BTREE,
  UNIQUE INDEX `UK_9yf05uuvsv9orphypaeydia8m`(`subject_name`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_subject
-- ----------------------------
INSERT INTO `tbl_subject` VALUES ('1517258390951', 'Bangla');
INSERT INTO `tbl_subject` VALUES ('1517258398061', 'English');

-- ----------------------------
-- Table structure for tbl_user
-- ----------------------------
DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user`  (
  `user_uuid` bigint(20) NOT NULL,
  `accountNonExpired` bit(1) NOT NULL,
  `accountNonLocked` bit(1) NOT NULL,
  `credentialsNonExpired` bit(1) NOT NULL,
  `email` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `enabled` bit(1) NOT NULL,
  `name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `username` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`user_uuid`) USING BTREE,
  UNIQUE INDEX `UK_npn1wf1yu1g5rjohbek375pp1`(`email`) USING BTREE,
  UNIQUE INDEX `UK_k0bty7tbcye41jpxam88q5kj2`(`username`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_user
-- ----------------------------
INSERT INTO `tbl_user` VALUES ('1517136119029', '1', '1', '1', 'admin@gmail.com', '1', 'Admin', '$2a$11$g6DNkV8GVS42NZ1EiJOKkex2Ggmg2tL8ruxHJMtqyMzGsOeDEf5bu', 'admin');
INSERT INTO `tbl_user` VALUES ('1517235954545', '1', '1', '1', 'ashique@gmail.com', '1', 'Ashique', '$2a$11$LsRgHCplUunRtXBJhURRM.dZOgOj3.T26OV65DaSakYopN1cRT.sK', 'ashique');
INSERT INTO `tbl_user` VALUES ('1517284898983', '1', '1', '1', 'teacher1@gmail.com', '1', 'Teacher1', '$2a$11$3xWYDjiI4xBRSM3/pFPAKeZnY7SWSZ76voGROBjQBdmE6RDsNxepS', 'teacher1');

-- ----------------------------
-- Table structure for user_authority
-- ----------------------------
DROP TABLE IF EXISTS `user_authority`;
CREATE TABLE `user_authority`  (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  INDEX `FKoyw9c38d0pnj0culyr38njm4a`(`role_id`) USING BTREE,
  INDEX `FK9c5cgxxc84xnx4jkxft2tfqd1`(`user_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of user_authority
-- ----------------------------
INSERT INTO `user_authority` VALUES ('1517135734589', '1517162726118');
INSERT INTO `user_authority` VALUES ('1517136119029', '1517136134152');
INSERT INTO `user_authority` VALUES ('1517235954545', '1517162726118');
INSERT INTO `user_authority` VALUES ('1517284898983', '1517162726118');

SET FOREIGN_KEY_CHECKS = 1;
