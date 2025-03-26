import React from "react";
import { Link } from "react-router-dom";
import ApiService from "../service/ApiService";
import { FaTachometerAlt, FaExchangeAlt, FaList, FaBox, FaTruck, FaShoppingCart, FaDollarSign, FaUser, FaSignOutAlt } from 'react-icons/fa';
const logout = () => {
  ApiService.logout();
};

const Sidebar = () => {
  const isAuth = ApiService.isAuthenticated();
  const isAdmin = ApiService.isAdmin();

  return (
    <div className="sidebar">
      <h1 className="ims">IMS</h1>
      <ul className="nav-links">
        {isAuth && (
          <li>
            <Link to="/dashboard">
              <FaTachometerAlt /> Dashboard
            </Link>
          </li>
        )}
  
        {isAuth && (
          <li>
            <Link to="/transaction">
              <FaExchangeAlt /> Transactions
            </Link>
          </li>
        )}
  
        {isAdmin && (
          <li>
            <Link to="/category">
              <FaList /> Category
            </Link>
          </li>
        )}
  
        {isAdmin && (
          <li>
            <Link to="/product">
              <FaBox /> Product
            </Link>
          </li>
        )}
  
        {isAdmin && (
          <li>
            <Link to="/supplier">
              <FaTruck /> Supplier
            </Link>
          </li>
        )}
  
        {isAuth && (
          <li>
            <Link to="/purchase">
              <FaShoppingCart /> Purchase
            </Link>
          </li>
        )}
  
        {isAuth && (
          <li>
            <Link to="/sell">
              <FaDollarSign /> Sell
            </Link>
          </li>
        )}
        {isAdmin && (
          <li>
            <Link to="/manager">
              <FaShoppingCart /> Managers
            </Link>
          </li>
        )}
  
        {isAuth && (
          <li>
            <Link to="/profile">
              <FaUser /> Profile
            </Link>
          </li>
        )}
  
        {isAuth && (
          <li>
            <Link onClick={logout} to="/login">
              <FaSignOutAlt /> Logout
            </Link>
          </li>
        )}
      </ul>
    </div>
  );
  
};

export default Sidebar;
