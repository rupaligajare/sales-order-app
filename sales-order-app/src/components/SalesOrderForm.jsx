import React, { useState } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../config/api';

export default function SalesOrderForm() {
  const [header, setHeader] = useState({
    Order_Number: '', Order_Key_Company: '', Order_Type: '', Currency_Code: '',
    Business_Unit: '', Long_Address_Number_ShipTo: '', Long_Address_Number_SoldTo: '', 
    Order_Date: '', Requested: ''
  });
  const [lines, setLines] = useState([{ Item_Number: '', Quantity_Ordered: '', Unit_Price: '', Branch__Plant: '', UoM: '' }]);

  const handleInputChange = (e) => setHeader({ ...header, [e.target.name]: e.target.value });
  const handleLineChange = (i, e) => {
    const newLines = [...lines];
    newLines[i][e.target.name] = e.target.value;
    setLines(newLines);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API_BASE_URL}/api/orders/create`, { ...header, orderLines: lines });
      alert('Order Submitted Successfully!');
    } catch (err) { alert('Submission Failed: ' + err.message); }
  };

  // Inline styles
  const containerStyle = { padding: '20px', maxWidth: '800px', margin: 'auto', fontFamily: 'Arial, sans-serif' };
  const gridStyle = { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '20px' };
  const inputStyle = { width: '100%', padding: '8px', boxSizing: 'border-box' };

  return (
    <div style={containerStyle}>
      <h2 style={{ color: '#333' }}>Create JDE Sales Order</h2>
      <form onSubmit={handleSubmit}>
        <h3>Header Information</h3>
        <div style={gridStyle}>
          <input name="Order_Number" placeholder="Order Number" onChange={handleInputChange} style={inputStyle} />
          <input name="Order_Key_Company" placeholder="Order Key Company" onChange={handleInputChange} style={inputStyle} />
          <input name="Order_Type" placeholder="Order Type" onChange={handleInputChange} style={inputStyle} />
          <input name="Currency_Code" placeholder="Currency Code" onChange={handleInputChange} style={inputStyle} />
          <input name="Business_Unit" placeholder="Business Unit" onChange={handleInputChange} style={inputStyle} />
          <input name="Long_Address_Number_ShipTo" placeholder="Ship To Number" onChange={handleInputChange} style={inputStyle} />
          <input name="Long_Address_Number_SoldTo" placeholder="Sold To Number" onChange={handleInputChange} style={inputStyle} />
          <input type="date" name="Order_Date" onChange={handleInputChange} style={inputStyle} />
          <input type="date" name="Requested" onChange={handleInputChange} style={inputStyle} />
        </div>
        
        <h3>Order Details</h3>
        {lines.map((l, i) => (
          <div key={i} style={{ display: 'flex', gap: '5px', marginBottom: '10px' }}>
            <input name="Item_Number" placeholder="Item #" onChange={(e) => handleLineChange(i, e)} style={inputStyle} />
            <input name="Quantity_Ordered" placeholder="Qty" type="number" onChange={(e) => handleLineChange(i, e)} style={inputStyle} />
            <input name="Unit_Price" placeholder="Price" type="number" onChange={(e) => handleLineChange(i, e)} style={inputStyle} />
            <input name="Branch__Plant" placeholder="Branch/Plant" onChange={(e) => handleLineChange(i, e)} style={inputStyle} />
            <input name="UoM" placeholder="UoM" onChange={(e) => handleLineChange(i, e)} style={inputStyle} />
          </div>
        ))}
        <button type="submit" style={{ padding: '10px 20px', background: '#007BFF', color: '#fff', border: 'none', borderRadius: '4px' }}>Submit Order</button>
      </form>
    </div>
  );
}