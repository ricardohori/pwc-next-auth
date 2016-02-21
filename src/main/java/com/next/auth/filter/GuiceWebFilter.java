package com.next.auth.filter;

import com.google.inject.servlet.GuiceFilter;

import javax.servlet.annotation.WebFilter;

/**
 * User: rfh
 * Date: 19-10-2015
 *
 * Configures GuiceFilter for all url mappings
 */
@WebFilter(urlPatterns = {"/*"})
public class GuiceWebFilter extends GuiceFilter {

}
