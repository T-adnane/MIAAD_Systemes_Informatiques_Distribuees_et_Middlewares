using System;

namespace DotNet_Client_Soap
{
    class Program
    {
        static void Main(string[] args)
        {
            ServiceReference1.BanqueServiceClient stub =
            new ServiceReference1.BanqueServiceClient();
            Console.WriteLine("-------- Conversion ---------");
            Console.WriteLine("1250 DH = "+stub.Convert(1250)+" EUR");
            Console.WriteLine("\n");
            Console.WriteLine("-------- Consulter un Compte ---------");
            ServiceReference1.compte cp = stub.getCompte(2);
            Console.WriteLine("\n");
            Console.WriteLine("Le Solde De Votre Compte est =" + cp.solde+" DH");
            Console.WriteLine("\n");
            Console.WriteLine("-------- Liste des comptes ---------");
            ServiceReference1.compte[] cptes = stub.listComptes();
            for (int i = 0; i < cptes.Length; i++)
            {
                Console.WriteLine("Code : " +cptes[i].code + "--- SOLDE =" + cptes[i].solde);
            }
            Console.ReadLine();
        }
    }
}