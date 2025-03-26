import React, { useState, useEffect } from "react";
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const TransactionDetailsPage = () => {
  const { transactionId } = useParams();
  const [transaction, setTransaction] = useState(null);
  const [message, setMessage] = useState("");
  const [status, setStatus] = useState("");
  const [errors, setErrors] = useState({}); // For validation errors

  const navigate = useNavigate();

  useEffect(() => {
    const getTransaction = async () => {
      try {
        const transactionData = await ApiService.getTransactionById(transactionId);

        if (transactionData.status === 200 && transactionData.transaction) {
          setTransaction(transactionData.transaction);
          setStatus(transactionData.transaction.status);
        } else {
          showMessage("Transaction not found.");
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error Getting transaction: " + error
        );
      }
    };

    getTransaction();
  }, [transactionId]);

  // Validate before updating transaction status
  const validateStatusUpdate = () => {
    let validationErrors = {};

    if (!transaction) {
      validationErrors.transaction = "Transaction data is missing.";
    } else if (status === transaction.status) {
      validationErrors.status = "Status is already set to the selected value.";
    }

    setErrors(validationErrors);
    return Object.keys(validationErrors).length === 0;
  };

  // Update transaction status
  const handleUpdateStatus = async () => {
    if (!validateStatusUpdate()) return;

    try {
      await ApiService.updateTransactionStatus(transactionId, status);
      showMessage("Transaction status updated successfully.");
      navigate("/transaction");
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error Updating transaction: " + error
      );
    }
  };

  // Show message or error
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <p className="message">{message}</p>}
      <div className="transaction-details-page">
        {transaction ? (
          <>
            {/* Transaction base information */}
            <div className="section-card">
              <h2>Transaction Information</h2>
              <p>Type: {transaction.transactionType}</p>
              <p>Status: {transaction.status}</p>
              <p>Description: {transaction.description || "N/A"}</p>
              <p>Note: {transaction.note || "No additional notes"}</p>
              <p>Total Products: {transaction.totalProducts}</p>
              <p>Total Price: {transaction.totalPrice.toFixed(2)}</p>
              <p>Created At: {new Date(transaction.createdAt).toLocaleString()}</p>

              {transaction.updatedAt && (
                <p>Updated At: {new Date(transaction.updatedAt).toLocaleString()}</p>
              )}
            </div>

            {/* Product information */}
            {transaction.product && (
              <div className="section-card">
                <h2>Product Information</h2>
                <p>Name: {transaction.product.name}</p>
                <p>SKU: {transaction.product.sku}</p>
                <p>Price: {transaction.product.price.toFixed(2)}</p>
                <p>Stock Quantity: {transaction.product.stockQuantity}</p>
                <p>Description: {transaction.product.description || "N/A"}</p>

                {transaction.product.imageUrl && (
                  <img src={transaction.product.imageUrl} alt={transaction.product.name} />
                )}
              </div>
            )}

            {/* User information */}
            {transaction.user && (
              <div className="section-card">
                <h2>User Information</h2>
                <p>Name: {transaction.user.name}</p>
                <p>Email: {transaction.user.email}</p>
                <p>Phone Number: {transaction.user.phoneNumber || "Not Provided"}</p>
                <p>Role: {transaction.user.role}</p>
                <p>Created At: {new Date(transaction.createdAt).toLocaleString()}</p>
              </div>
            )}

            {/* Supplier information */}
            {transaction.supplier && (
              <div className="section-card">
                <h2>Supplier Information</h2>
                <p>Name: {transaction.supplier.name}</p>
                <p>Contact Address: {transaction.supplier.contactInfo || "Not Provided"}</p>
                <p>Address: {transaction.supplier.address || "Not Available"}</p>
              </div>
            )}

            {/* Update Transaction Status */}
            <div className="section-card transaction-status-update">
              <label>Status: </label>
              <select value={status} onChange={(e) => setStatus(e.target.value)}>
                <option value="PENDING">PENDING</option>
                <option value="PROCESSING">PROCESSING</option>
                <option value="COMPLETED">COMPLETED</option>
                <option value="CANCELLED">CANCELLED</option>
              </select>
              {errors.status && <span className="error">{errors.status}</span>}
              <button onClick={handleUpdateStatus} disabled={status === transaction.status}>
                Update Status
              </button>
            </div>
          </>
        ) : (
          <p>Loading transaction details...</p>
        )}
      </div>
    </Layout>
  );
};

export default TransactionDetailsPage;
