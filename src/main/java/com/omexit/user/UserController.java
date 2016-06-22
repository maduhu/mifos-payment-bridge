package com.omexit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * The UserController class is a RESTful web service controller. The
 * <code>@RestController</code> annotation informs Spring that each
 * <code>@RequestMapping</code> method returns a <code>@ResponseBody</code>.
 *
 * @author Antony Omeri
 */
@RestController
@RequestMapping(value="/api/1.0")
public class UserController  {
    @Autowired
    UserService userService;

    /**
     * Web service endpoint to fetch all User entities. The service returns the collection of User entity in JSON
     *
     * @return A ResponseEntity containing a Collection of User Objects
     */
    @RequestMapping(
            value = "/user",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<User>> getUsers() {
        Collection<User> users = userService.findAll();

        return new ResponseEntity<Collection<User>>(users, HttpStatus.OK);
    }


    /**
     * Web service endpoint to fetch a single User entity by primary key identifier
     * <p>
     * If found the User is returned as JSON with HTTP status 200
     * <p>
     * If not found, the service returns an empty response body with HTTP status 404
     *
     * @param id
     * @return A ResponseEntity containing a single User object,
     */
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUsers(@PathVariable Long id)  {
        User user = userService.findUserById(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    /**
     * Web service endpoint to create a single User entity. The HTTP request
     * body is expected to contain a User object in JSON format. The
     * User is persisted in the data repository.
     * <p>
     * If created successfully, the persisted User is returned as JSON with
     * HTTP status 201.
     * <p>
     * If not created successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param user The User object to be created.
     * @return A ResponseEntity containing a single User object, if created
     * successfully, and a HTTP status code as described in the method
     * comment.
     * @throws Exception Thrown if a problem occurs completing the request.
     */
    @RequestMapping(
            value = "/user",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.create(user);

        return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Web service endpoint to update a single User entity. The HTTP request
     * body is expected to contain a User object in JSON format. The
     * User is updated in the data repository.
     * <p>
     * If updated successfully, the persisted User is returned as JSON with
     * HTTP status 200.
     * <p>
     * If not found, the service returns an empty response body and HTTP status
     * 404.
     * <p>
     * If not updated successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param user The User object to be updated.
     * @return A ResponseEntity containing a single User object, if updated
     * successfully, and a HTTP status code as described in the method
     * comment.
     * @throws Exception Thrown if a problem occurs completing the request.
     */
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.update(user);

        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }
}
