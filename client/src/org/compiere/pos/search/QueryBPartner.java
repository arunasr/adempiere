/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.               *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package org.compiere.pos.search;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.compiere.grid.ed.VBPartner;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MBPartnerInfo;
import org.compiere.pos.POSTextField;
import org.compiere.pos.PosTable;
import org.compiere.pos.VPOS;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CScrollPane;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *	POS Query BPartner
 *	
 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright (c) Jorg Janke
 *  @author Dixon Martinez, ERPCYA 
 *  @author Susanne Calderón Schöningh, Systemhaus Westfalia
 *  
 *  @version $Id: QueryBPartner.java,v 1.1 2004/07/12 04:10:04 jjanke Exp $
 *  @version $Id: QueryBPartner.java,v 2.0 2015/09/01 00:00:00 scalderon
 */
public class QueryBPartner extends POSQuery
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7109518709654253628L;

    //	Support for creating customers from the point of sale
	private CButton bot_New;
	
	/**
	 * 	Constructor
	 */
	public QueryBPartner (VPOS posPanel)
	{
		super(posPanel);
	}	//	PosQueryBPartner
	
	private POSTextField		f_value;
	private POSTextField		f_name;
	private POSTextField		f_contact;
	private POSTextField		f_email;
	private POSTextField		f_phone;
	private CTextField		f_city;

	private int				m_C_BPartner_ID;
	private CButton f_refresh;
	private CButton f_ok;
	private CButton f_cancel;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(QueryBPartner.class);
	
	
	/**	Table Column Layout Info			*/
	private static ColumnInfo[] s_layout = new ColumnInfo[] 
	{
		new ColumnInfo(" ", "C_BPartner_ID", IDColumn.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Value"), "Value", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "Name", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Email"), "Email", String.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "Phone"), "Phone", String.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "Postal"), "Postal", String.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "City"), "City", String.class) 
	};
	/**	From Clause							*/
	private static String s_sqlFrom = "RV_BPartner";
	/** Where Clause						*/
	private static String s_sqlWhere = "IsActive='Y'"; 

	/**
	 * 	Set up Panel
	 */
	protected void init()
	{
		CPanel panel = new CPanel();
		
		panel.setLayout(new MigLayout("fill"));
		getContentPane().add(panel);
		//	North
		northPanel = new CPanel(new MigLayout("fill","", "[50][50][]"));
		panel.add (northPanel, "north");
		northPanel.setBorder(new TitledBorder(Msg.getMsg(p_ctx, "Query")));
		
		CLabel lvalue = new CLabel(Msg.translate(p_ctx, "Value"));
		northPanel.add (lvalue, " growy");
		f_value = new POSTextField("", v_POSPanel.getKeyboard());
		lvalue.setLabelFor(f_value);
		northPanel.add(f_value, "h 30, w 200");
		f_value.addActionListener(this);
		
		//
		CLabel lcontact = new CLabel(Msg.translate(p_ctx, "Contact"));
		northPanel.add (lcontact, " growy");
		f_contact = new POSTextField("", v_POSPanel.getKeyboard());
		lcontact.setLabelFor(f_contact);
		northPanel.add(f_contact, "h 30, w 200");
		f_contact.addActionListener(this);
		
		//
		CLabel lphone = new CLabel(Msg.translate(p_ctx, "Phone"));
		northPanel.add (lphone, " growy");
		f_phone = new POSTextField("", v_POSPanel.getKeyboard());
		lphone.setLabelFor(f_phone);
		northPanel.add(f_phone, "h 30, w 200, wrap");
		f_phone.addActionListener(this);
		
		//
		CLabel lname = new CLabel(Msg.translate(p_ctx, "Name"));
		northPanel.add (lname, " growy");
		f_name = new POSTextField("", v_POSPanel.getKeyboard());
		lname.setLabelFor(f_name);
		northPanel.add(f_name, "h 30, w 200");
		f_name.addActionListener(this);
		//
		CLabel lemail = new CLabel(Msg.translate(p_ctx, "Email"));
		northPanel.add (lemail, " growy");
		f_email = new POSTextField("", v_POSPanel.getKeyboard());
		lemail.setLabelFor(f_email);
		northPanel.add(f_email, "h 30, w 200");
		f_email.addActionListener(this);
		//
		CLabel lcity = new CLabel(Msg.translate(p_ctx, "City"));
		northPanel.add (lcity, " growy");
		f_city = new CTextField(10);
		lcity.setLabelFor(f_city);
		northPanel.add(f_city, "h 30, w 200");
		f_city.addActionListener(this);
		//
		
		f_refresh = createButtonAction("Refresh", KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		northPanel.add(f_refresh, "w 50!, h 50!, wrap, alignx trailing");

		// Support for creating customers from the point of sale
		bot_New = createButtonAction("New", KeyStroke.getKeyStroke(KeyEvent.VK_N, 0));
		northPanel.add(bot_New, "w 50!, h 50!");
		
		f_up = createButtonAction("Previous", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
		northPanel.add(f_up, "w 50!, h 50!, span, split 4");
		f_down = createButtonAction("Next", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
		northPanel.add(f_down, "w 50!, h 50!");
		
		f_ok = createButtonAction("Ok", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
		northPanel.add(f_ok, "w 50!, h 50!");
		
		f_cancel = createButtonAction("Cancel", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		northPanel.add(f_cancel, "w 50!, h 50!");
		
		//	Center
		m_table = new PosTable();
		String sql = m_table.prepareTable (s_layout, s_sqlFrom, 
			s_sqlWhere, false, "RV_BPartner")
			+ " ORDER BY Value";
		m_table.addMouseListener(this);
		m_table.getSelectionModel().addListSelectionListener(this);
		enableButtons();
		centerScroll = new CScrollPane(m_table);
		panel.add (centerScroll, "growx, growy");
		m_table.growScrollbars();
		panel.setPreferredSize(new Dimension(800,600));
		f_value.requestFocus();
	}	//	init
	
	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.info(e.getActionCommand());
		if ("Refresh".equals(e.getActionCommand())
			|| e.getSource() == f_value // || e.getSource() == f_upc
			|| e.getSource() == f_name // || e.getSource() == f_sku
			)
		{
			setResults(MBPartnerInfo.find (p_ctx,
				f_value.getText(), f_name.getText(), 
				null, f_email.getText(),
				f_phone.getText(), f_city.getText()));
			return;
		}
		else if ("Reset".equals(e.getActionCommand()))
		{
			reset();
			return;
		}
		else if ("Previous".equalsIgnoreCase(e.getActionCommand()))
		{
			int rows = m_table.getRowCount();
			if (rows == 0)
				return;
			int row = m_table.getSelectedRow();
			row--;
			if (row < 0)
				row = 0;
			m_table.getSelectionModel().setSelectionInterval(row, row);
			return;
		}
		else if ("Next".equalsIgnoreCase(e.getActionCommand()))
		{
			int rows = m_table.getRowCount();
			if (rows == 0)
				return;
			int row = m_table.getSelectedRow();
			row++;
			if (row >= rows)
				row = rows - 1;
			m_table.getSelectionModel().setSelectionInterval(row, row);
			return;
		}
		// Support for creating customers from the point of sale
		else if("New".equalsIgnoreCase(e.getActionCommand())) {
			
			VBPartner t = new VBPartner(new Frame(), 0);
			t.setVisible(true);
			
			m_C_BPartner_ID = t.getC_BPartner_ID();
			
			close();
		}
		//	Exit
		close();
	}	//	actionPerformed
	
	
	/**
	 * 	Set/display Results
	 *	@param results results
	 */
	public void setResults (MBPartnerInfo[] results)
	{
		m_table.loadTable(results);
		if (m_table.getRowCount() >0 )
			m_table.setRowSelectionInterval(0, 0);
		enableButtons();
	}	//	setResults

	/**
	 * 	Enable/Set Buttons and set ID
	 */
	protected void enableButtons()
	{
		m_C_BPartner_ID = -1;
		int row = m_table.getSelectedRow();
		boolean enabled = row != -1;
		if (enabled)
		{
			Integer ID = m_table.getSelectedRowKey();
			if (ID != null)
			{
				m_C_BPartner_ID = ID.intValue();
			//	m_BPartnerName = (String)m_table.getValueAt(row, 2);
			//	m_Price = (BigDecimal)m_table.getValueAt(row, 7);
			}
		}
		f_ok.setEnabled(enabled);
		log.fine("C_BPartner_ID=" + m_C_BPartner_ID); 
	}	//	enableButtons

	/**
	 * 	Close.
	 * 	Set Values on other panels and close
	 */
	protected void close() {
		Integer ID = m_table.getSelectedRowKey();
			if (ID != null)
				m_C_BPartner_ID = ID.intValue();
		
		if (m_C_BPartner_ID > 0) {
			v_POSPanel.setC_BPartner_ID(m_C_BPartner_ID);
			log.fine("C_BPartner_ID=" + m_C_BPartner_ID);
		} else {
			v_POSPanel.setC_BPartner_ID(0);
		}
		dispose();
	}	//	close


	@Override
	public void reset() {
		f_value.setText(null);
		f_name.setText(null);
		f_contact.setText(null);
		f_email.setText(null);
		f_phone.setText(null);
		f_city.setText(null);
		setResults(new MBPartnerInfo[0]);
	}
}	//	PosQueryBPartner