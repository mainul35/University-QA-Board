package com.springprojects.controller;

public class UploadController {
//	@RequestMapping(value = "/albums/{id}/upload", method = RequestMethod.POST)
////  @PostMapping("/upload") // //new annotation since 4.3
//  public String upload_POST(
//          @ModelAttribute("screencast") ScreenCast screenCast,
//          @RequestParam("file") MultipartFile file,
//          @RequestParam("uploadTime") String date,
//          @PathVariable("id") Long albumId,
//          HttpSession session,
//          Model model) {
//          model.addAttribute("username", session.getAttribute("username").toString());
//      if (file.isEmpty()) {
//          logger.info("File empty");
//          return "redirect:/albums/" + albumId + "/upload";
//      }
//
//      Long id = Long.parseLong(session.getAttribute("id").toString());
//      screenCast.setId(System.currentTimeMillis());
//      screenCast = fileService.upload(screenCast, file, id);
////      screenCast.
//      System.out.println(date);
//
//      screenCast.setDate(Utils.convertToDate(date));
//      logger.info("At line 71 : " + screenCast.toString());
//      Album album = albumService.getAlbumById(albumId);
//      screenCast.setSubject(album.getTag());
//      screenCast.setAlbum(album);
//      album.getScreenCasts().add(screenCast);
//      UserEntity userEntity = userService.getUserById(id);
//      userEntity.getAlbums().add(album);
//      userEntity.getScreenCasts().add(screenCast);
//      album.setUserEntity(userEntity);
//      albumService.save(album);
////      userService.createUser(userEntity);
//      return "redirect:/view-album/" + albumId;
//  }
}
