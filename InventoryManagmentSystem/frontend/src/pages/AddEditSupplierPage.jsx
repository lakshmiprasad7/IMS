import React, { useState, useEffect } from "react";
import { FaUser, FaPhone, FaMapMarkerAlt } from "react-icons/fa"; // Importing react-icons
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const AddEditSupplierPage = () => {
  const { supplierId } = useParams("");
  const [name, setName] = useState("");
  const [contactInfo, setContactInfo] = useState("");
  const [address, setAddress] = useState("");
  const [message, setMessage] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [errors, setErrors] = useState({}); // State for validation errors

  const navigate = useNavigate();

  useEffect(() => {
    if (supplierId) {
      setIsEditing(true);

      const fetchSupplier = async () => {
        try {
          const supplierData = await ApiService.getSupplierById(supplierId);
          if (supplierData.status === 200) {
            setName(supplierData.supplier.name);
            setContactInfo(supplierData.supplier.contactInfo);
            setAddress(supplierData.supplier.address);
          }
        } catch (error) {
          showMessage(
            error.response?.data?.message ||
              "Error getting a supplier by ID: " + error
          );
        }
      };
      fetchSupplier();
    }
  }, [supplierId]);

  // Validation function
  const validateForm = () => {
    let validationErrors = {};
    
    if (!name.trim()) {
      validationErrors.name = "Name is required";
    } else if (!/^[A-Za-z\s]{3,}$/.test(name)) {
      validationErrors.name = "Name must be at least 3 characters and contain only letters and spaces";
    }

    if (!contactInfo.trim()) {
      validationErrors.contactInfo = "Phone number is required";
    } else if (!/^\d{10}$/.test(contactInfo)) {
      validationErrors.contactInfo = "Phone number must be exactly 10 digits";
    }

    setErrors(validationErrors);
    return Object.keys(validationErrors).length === 0; // Return true if no errors
  };

  // Handle form submission for both add and edit supplier
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return; // Stop submission if validation fails

    const supplierData = { name, contactInfo, address };

    try {
      if (isEditing) {
        await ApiService.updateSupplier(supplierId, supplierData);
        showMessage("Supplier edited successfully");
        navigate("/supplier");
      } else {
        await ApiService.addSupplier(supplierData);
        showMessage("Supplier added successfully");
        navigate("/supplier");
      }
    } catch (error) {
      showMessage(
        error.response?.data?.message ||
          "Error adding or editing supplier: " + error
      );
    }
  };

  // Method to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="supplier-form-page">
        <h1>{isEditing ? "Edit Supplier" : "Add Supplier"}</h1>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>
              <FaUser /> Supplier Name
            </label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              type="text"
            />
            {errors.name && <span className="error">{errors.name}</span>}
          </div>

          <div className="form-group">
            <label>
              <FaPhone /> Contact Info
            </label>
            <input
              value={contactInfo}
              onChange={(e) => setContactInfo(e.target.value)}
              required
              type="text"
            />
            {errors.contactInfo && (
              <span className="error">{errors.contactInfo}</span>
            )}
          </div>

          <div className="form-group">
            <label>
              <FaMapMarkerAlt /> Address
            </label>
            <input
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              required
              type="text"
            />
          </div>
          <button type="submit">
            {isEditing ? "Edit Supplier" : "Add Supplier"}
          </button>
        </form>
      </div>
    </Layout>
  );
};

export default AddEditSupplierPage;
