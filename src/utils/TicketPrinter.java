package utils;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import models.Ticket;

public class TicketPrinter implements Printable {

    private Ticket ticket;

    public TicketPrinter(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        int y = 20;
        for (String line : ticket.getTicketInfo().split("\n")) {
            g.drawString(line, 10, y);
            y += 15;
        }

        return PAGE_EXISTS;
    }

    public void printTicket() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) { // tampilkan dialog print
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal mencetak tiket!");
            }
        }
    }
}