package telvoterminal.telvo.com.terminal.service;



public interface IServiceResultListener {
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success);
}
