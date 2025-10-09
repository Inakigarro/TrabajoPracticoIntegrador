package IG.domain.Clases;

import IG.application.Dtos.OrdenMovimiento.OrdenMovimientoDto;
import IG.domain.Enums.TipoMovimiento;
import IG.domain.Enums.EstadosOrdenes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

 public class OrdenMovimiento {
    private Integer id;
    private TipoMovimiento tipo;
    private LocalDateTime fecha;
    private EstadosOrdenes estado;
    private EstadoOrdenMovimiento estadoOrden;
    private List<DetalleMovimiento> detalleMovimientoList;

    public OrdenMovimiento() {
        id = 0;
        tipo = TipoMovimiento.SINDEFINIR;
        fecha = LocalDateTime.now();
        estado = EstadosOrdenes.PENDIENTE;
        estadoOrden = new PendienteEstado();
        detalleMovimientoList = new ArrayList<>();
    }

    public OrdenMovimiento(Integer id, TipoMovimiento tipo, LocalDateTime fecha, EstadosOrdenes estado, List<DetalleMovimiento> detalleMovimientoList) {
        this();
        this.setId(id);
        this.setTipo(tipo);
        this.setFecha(fecha);
        this.setEstado(estado);
        this.agregarDetalles(detalleMovimientoList);
        switch (estado) {
            case PENDIENTE -> estadoOrden = new PendienteEstado();
            case PROCESO -> estadoOrden = new EnProcesoEstado();
            case COMPLETADO -> estadoOrden = new CompletadaEstado();
            case CANCELADO -> estadoOrden = new CanceladaEstado();
            default -> estadoOrden = new PendienteEstado();
        }
    }

    public void aprobar() {
        estadoOrden.aprobar(this);
    }
    public void ejecutar() throws Exception{
        estadoOrden.ejecutar(this);
    }
    public void completar() {
        estadoOrden.completar(this);
    }
    public void cancelar() {
        estadoOrden.cancelar(this);
    }
    public EstadoOrdenMovimiento getEstadoOrden() {
        return estadoOrden;
    }
    public void setEstadoOrden(EstadoOrdenMovimiento estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
         if (id == null || id < 0)
            throw new IllegalArgumentException("El id no puede ser nulo.");

         this.id = id;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        if (tipo == null || tipo == TipoMovimiento.SINDEFINIR)
            throw new IllegalArgumentException("El tipo no puede ser nulo");

        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        if (fecha == null)
            throw new IllegalArgumentException("La fecha no puede ser nula");

     this.fecha = fecha;
    }

    public EstadosOrdenes getEstado() {
        return estado;
    }

    public void setEstado(EstadosOrdenes estado) {
        this.estado = estado;
    }

    public void agregarDetalle(DetalleMovimiento detalle){
        detalleMovimientoList.add(detalle);
        detalle.setOrdenMovimiento(this);
    }

    public void agregarDetalles(List<DetalleMovimiento> detalles){
        if (detalles == null)
            throw new IllegalArgumentException("La lista de detalles no puede ser nula.");

        for (DetalleMovimiento detalle : detalles) {
            agregarDetalle(detalle);
        }
    }

    public List<DetalleMovimiento> getDetalleMovimientoList() {
        return detalleMovimientoList;
    }

    public static OrdenMovimiento map(OrdenMovimientoDto dto) {
        return new OrdenMovimiento(
                dto.id(),
                dto.tipo(),
                dto.fecha(),
                dto.estado(),
                dto.detalleMovimientoList() != null ? dto.detalleMovimientoList().stream().map(DetalleMovimiento::map).toList() : new ArrayList<>()
        );
    }

    @Override
    public String toString() {
        return String.format("%1$d - %2$s - %3$s - %4$s - %5$s",
                this.id, this.tipo, this.fecha, this.estado, this.detalleMovimientoList);
    }
 }